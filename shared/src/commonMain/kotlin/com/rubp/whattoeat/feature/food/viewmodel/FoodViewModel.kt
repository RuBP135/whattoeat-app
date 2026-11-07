package com.rubp.whattoeat.feature.food.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubp.whattoeat.core.ui.state.ReadState
import com.rubp.whattoeat.core.ui.feedback.UiFeedback
import com.rubp.whattoeat.core.ui.state.ReadStateMapper
import com.rubp.whattoeat.core.ui.task.UiTaskRunner
import com.rubp.whattoeat.feature.food.data.entity.Food
import com.rubp.whattoeat.feature.food.data.entity.FoodTable
import com.rubp.whattoeat.feature.food.data.preferences.FoodPreferences
import com.rubp.whattoeat.feature.food.data.repository.FoodRepository
import com.rubp.whattoeat.feature.food.data.repository.FoodTableRepository
import com.rubp.whattoeat.feature.food.domain.exception.FoodNotFoundException
import com.rubp.whattoeat.feature.food.domain.exception.FoodTableNotFoundException
import com.rubp.whattoeat.feature.food.domain.exception.InvalidFoodTableJsonException
import com.rubp.whattoeat.feature.food.domain.exception.NoFoodSelectedException
import com.rubp.whattoeat.feature.food.domain.exception.NoTableSelectedException
import com.rubp.whattoeat.feature.food.domain.foodTableToJson
import com.rubp.whattoeat.feature.food.domain.jsonToFoodTableDto
import com.rubp.whattoeat.feature.food.domain.selectFood
import com.rubp.whattoeat.feature.food.model.FoodDataState
import com.rubp.whattoeat.feature.food.model.FoodSelectionIdState
import com.rubp.whattoeat.feature.food.model.FoodSelectionState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.SerializationException

@OptIn(ExperimentalCoroutinesApi::class)
class FoodViewModel(
    private val foodRepository: FoodRepository,
    private val foodTableRepository: FoodTableRepository,
    private val foodPreferences: FoodPreferences,
    feedback: UiFeedback
) : ViewModel() {

    private val mutex = Mutex()
    private val tag = "FoodViewModel"
    private val mapper = ReadStateMapper(tag)
    private val runner = UiTaskRunner(
        scope = viewModelScope,
        tag = tag,
        onMessage = feedback::showMessage,
        onError = feedback::showError
    )

    private val selectionIdStateFlow: MutableStateFlow<FoodSelectionIdState> =
        MutableStateFlow(FoodSelectionIdState.Idle)

    private val retryTrigger = MutableStateFlow(0)

    /**
     * 仅用于渲染 ui
     */
    val foodDataStateFlow = retryTrigger
        .flatMapLatest { createFoodDataFlow() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = FoodDataState.Loading
        )

    init {
        launchLocked(
            errorLogMessage = "Failed to initialize default food table"
        ) {
            if(foodTableRepository.findAllOnce().isEmpty()) {
                createTableInternal("默认表格")
            }
        }
    }

    fun retry() {
        retryTrigger.update { it + 1 }
    }

    fun insertFood(food: Food) = launchLocked(
        errorLogMessage = "Failed to insert food"
    ) {
        val id = requireCurrentTableId()
        foodRepository.insert(food.copy(tableId = id)) // ui 层不提供 table_id，在这里补充。
    }

    fun updateName(id: Long, name: String) = launchLocked(
        errorLogMessage = "Failed to update food name: foodId=$id"
    ) {
        if (foodRepository.updateName(id, name) == 0) {
            throw FoodNotFoundException(id)
        }
    }

    fun updateWeight(id: Long, weight: Int) = launchLocked(
        errorLogMessage = "Failed to update food weight: foodId=$id, weight=$weight"
    ) {
        if (foodRepository.updateWeight(id, weight) == 0) {
            throw FoodNotFoundException(id)
        }
    }

    fun updateMarked(id: Long, marked: Boolean) = launchLocked(
        errorLogMessage = "Failed to update food marked state: foodId=$id, marked=$marked"
    ) {
        if (foodRepository.updateMarked(id, marked) == 0) {
            throw FoodNotFoundException(id)
        }
    }

    fun deleteFoodById(id: Long) = launchLocked(
        errorLogMessage = "Failed to delete food: foodId=$id",
        successMessage = "删除成功"
    ) {
        if (foodRepository.deleteById(id) == 0) throw FoodNotFoundException(id)
        selectionIdStateFlow.update { current ->
            if (current is FoodSelectionIdState.Chosen && current.id == id)
                FoodSelectionIdState.Idle
            else
                current
        }
    }

    fun chosenRandomFood() = launchLocked(
        errorLogMessage = "Failed to choose random food"
    ) {
        val tableId = requireCurrentTableId()
        val foods = foodRepository.findByTableIdOnce(tableId)

        val candidateList = foods.filter { food ->
            food.marked && food.weight > 0 && food.name.isNotEmpty()
        }

        val selectionState = selectionIdStateFlow.value
        val preId = (selectionState as? FoodSelectionIdState.Chosen)?.id
        val id = selectFood(candidateList, preId)

        if (id == null) selectionIdStateFlow.value = FoodSelectionIdState.NoAvailableFood
        else selectionIdStateFlow.value = FoodSelectionIdState.Chosen(id)
    }

    fun ignoreChosenFood() = launchLocked(
        errorLogMessage = "Failed to ignore selected food",
        successMessage = "已忽略当前选择食物"
    ) {
        val selectionState = selectionIdStateFlow.value
        val id = (selectionState as? FoodSelectionIdState.Chosen)?.id ?: throw NoFoodSelectedException()

        val update = foodRepository.updateMarked(id, false)
        if (update == 0) throw FoodNotFoundException(id)
    }

    fun recoverAll() = launchLocked(
        errorLogMessage = "Failed to restore all ignored foods",
        successMessage = "已恢复所有被忽略的食物"
    ) {
        val id = requireCurrentTableId()
        foodRepository.updateAllMarked(id, marked = true)
    }

    fun clearSelection() = launchLocked(
        errorLogMessage = "Failed to clear food selection",
        successMessage = "已清除当前选择"
    ) {
        selectionIdStateFlow.value = FoodSelectionIdState.Idle
    }

    // table
    fun exportTable(
        onExport: suspend (String) -> Unit
    ) = launchLocked(
        errorLogMessage = "Failed to export food table",
        successMessage = "已导出json至剪贴板"
    ) {
        val id = foodPreferences.getFoodTableId() ?: throw NoTableSelectedException()
        val table = foodTableRepository.findByIdOnce(id) ?: throw FoodTableNotFoundException(id)
        val foods = foodRepository.findByTableIdOnce(id)
        onExport(foodTableToJson(table, foods))
    }

    fun importTable(json: String) = launchLocked(
        errorLogMessage = "Failed to import food table",
        successMessage = "已导入表格"
    ) {

        val foodTableDto = try {
            jsonToFoodTableDto(json)
        } catch (_: SerializationException) {
            throw InvalidFoodTableJsonException()
        }

        val id = foodTableRepository.insertWithFood(
            table = FoodTable(name = foodTableDto.name),
            foods = foodTableDto.foodDtos.map {
                Food(name = it.name, weight = it.weight, marked = true)
            }
        )

        if(getCurrentTableIdOrNull() == null) {
            foodPreferences.saveFoodTableId(id)
        }
    }

    fun switchTable(id: Long) = launchLocked(
        errorLogMessage = "Failed to switch food table: tableId=$id"
    ) {
        foodTableRepository.findByIdOnce(id) ?: throw FoodTableNotFoundException(id)
        val preId = foodPreferences.getFoodTableId()
        if(id == preId) return@launchLocked
        foodPreferences.saveFoodTableId(id)
        selectionIdStateFlow.value = FoodSelectionIdState.Idle
    }

    fun createTable(name: String) = launchLocked(
        errorLogMessage = "Failed to create food table"
    ) {
        createTableInternal(name)
    }

    fun renameTable(name: String) = launchLocked(
        errorLogMessage = "Failed to rename food table"
    ) {
        val id = requireCurrentTableId()
        if(foodTableRepository.updateName(id, name) == 0) {
            throw FoodTableNotFoundException(id)
        }
    }

    fun deleteTable() = launchLocked(
        errorLogMessage = "Failed to delete food table",
        successMessage = "已删除表格"
    ) {
        val id = requireCurrentTableId()
        val tables = foodTableRepository.findAllOnce()

        val index = tables.indexOfFirst { it.id == id }
        if(index == -1) throw FoodTableNotFoundException(id)

        if(foodTableRepository.deleteById(id) == 0)
            throw FoodTableNotFoundException(id)

        val newId: Long? = when {
            index - 1 >= 0 -> tables[index - 1].id
            index + 1 < tables.size -> tables[index + 1].id
            else -> null
        }

        foodPreferences.saveFoodTableId(newId)
        selectionIdStateFlow.value = FoodSelectionIdState.Idle
    }

    private fun createFoodDataFlow(): Flow<FoodDataState> =
        combine(
            mapper.map(
                flow = foodTableRepository.findAll(),
                errorLogMessage = "Failed to observe food tables"
            ),
            foodPreferences.foodTableIdFlow
        ) { tables, currentTableId ->
            tables to currentTableId
        }.transformLatest { (tables, currentTableId) ->
            when(tables) {
                is ReadState.Loading -> emit(FoodDataState.Loading)
                is ReadState.Error -> emit(FoodDataState.Error(tables.exception))
                is ReadState.Success -> {
                    val currentTable = tables.data.firstOrNull { it.id == currentTableId }
                    if(currentTable == null) emit(
                        FoodDataState.NoTableSelected(tables.data)
                    ) else emitAll(observeTableFoods(tables.data, currentTable))
                }
            }
        }

    private fun observeTableFoods(
        tables: List<FoodTable>,
        currentTable: FoodTable
    ): Flow<FoodDataState> = combine(
        mapper.map(
            flow = foodRepository.findByTableId(currentTable.id),
            errorLogMessage = "Failed to observe foods: tableId=${currentTable.id}"
        ),
        selectionIdStateFlow
    ) { foods, idState ->
        when(foods) {
            is ReadState.Loading -> FoodDataState.Loading
            is ReadState.Error -> FoodDataState.Error(foods.exception)
            is ReadState.Success -> FoodDataState.Selected(
                tables = tables,
                currentTable = currentTable,
                foods = foods.data,
                selectionState = resolveSelectionState(foods.data, idState)
            )
        }
    }

    private fun resolveSelectionState(
        foods: List<Food>,
        idState: FoodSelectionIdState
    ): FoodSelectionState = when(idState) {
        is FoodSelectionIdState.Idle -> FoodSelectionState.Idle
        is FoodSelectionIdState.NoAvailableFood -> FoodSelectionState.NoAvailableFood
        is FoodSelectionIdState.Chosen -> foods.find { it.id == idState.id }?.let {
            FoodSelectionState.Chosen(it)
        } ?: FoodSelectionState.Idle
    }

    private suspend fun createTableInternal(name: String) {
        val id = foodTableRepository.insertWithFood(
            table = FoodTable(name = name),
            foods = List(3) {
                Food(name = "", weight = 1, marked = true)
            }
        )
        if(getCurrentTableIdOrNull() == null) {
            foodPreferences.saveFoodTableId(id)
        }
    }

    /** 获取有效的当前表格 ID，无效返回 null */
    private suspend fun getCurrentTableIdOrNull(): Long? {
        val id = foodPreferences.getFoodTableId() ?: return null
        return foodTableRepository.findByIdOnce(id)?.id
    }

    /** 获取有效的当前表格 ID，并且会在无效时抛出异常 */
    private suspend fun requireCurrentTableId(): Long =
        getCurrentTableIdOrNull() ?: throw NoTableSelectedException()

    private fun launchLocked(
        successMessage: String? = null,
        errorLogMessage: String,
        action: suspend () -> Unit
    ) = runner.launch(
        successMessage = successMessage,
        errorLogMessage = errorLogMessage
    ) {
        mutex.withLock {
            action()
        }
    }
}
