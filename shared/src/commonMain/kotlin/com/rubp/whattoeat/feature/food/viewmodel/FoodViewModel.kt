package com.rubp.whattoeat.feature.food.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubp.whattoeat.core.model.ReadState
import com.rubp.whattoeat.core.ui.feedback.UiFeedback
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.serialization.SerializationException

@OptIn(ExperimentalCoroutinesApi::class)
class FoodViewModel(
    private val foodRepository: FoodRepository,
    private val foodTableRepository: FoodTableRepository,
    private val foodPreferences: FoodPreferences,
    feedback: UiFeedback
) : ViewModel() {

    private val runner = UiTaskRunner(
        scope = viewModelScope,
        onMessage = feedback::showMessage,
        onError = feedback::showError
    )

    private val selectionIdStateFlow: MutableStateFlow<FoodSelectionIdState> = MutableStateFlow(FoodSelectionIdState.Idle)

    /**
     * 仅用于渲染 ui
     */
    val foodDataStateFlow =
        combine(
            foodTableRepository.findAll(),
            foodPreferences.foodTableIdFlow
        ) { tables, currentTableId ->
            tables to currentTableId
        }.transformLatest { (tables, currentTableId) ->
            when(tables) {
                is ReadState.Loading -> emit(FoodDataState.Loading)
                is ReadState.Error -> emit(FoodDataState.Error(tables.exception))
                is ReadState.Success -> {
                    val currentTable = tables.data.firstOrNull {
                        it.id == currentTableId
                    }

                    if(currentTable == null) emit(
                        FoodDataState.NoTableSelected(tables.data)
                    ) else emitAll(
                        combine(
                            foodRepository.findByTableId(currentTable.id),
                            selectionIdStateFlow
                        ) { foods, idState ->
                            when(foods){
                                is ReadState.Loading -> FoodDataState.Loading
                                is ReadState.Error -> FoodDataState.Error(foods.exception)
                                is ReadState.Success -> FoodDataState.Selected(
                                    tables = tables.data,
                                    currentTable = currentTable,
                                    foods = foods.data,
                                    selectionState = when(idState) {
                                        is FoodSelectionIdState.Idle -> FoodSelectionState.Idle
                                        is FoodSelectionIdState.NoAvailableFood -> FoodSelectionState.NoAvailableFood
                                        is FoodSelectionIdState.Chosen -> foods.data.find { it.id == idState.id }?.let {
                                            FoodSelectionState.Chosen(it)
                                        } ?: FoodSelectionState.Idle
                                    }
                                )
                            }
                        }
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = FoodDataState.Loading
        )


    init {
        runner.launch {
            if(foodTableRepository.findAllOnce().isEmpty()) {
                createTable("默认表格")
            }
        }
    }


    fun insertFood(food: Food) {
        runner.launch {
            val id = foodPreferences.getFoodTableId() ?:throw NoTableSelectedException()
            foodTableRepository.findByIdOnce(id) ?: throw NoTableSelectedException()
            foodRepository.insert(food.copy(tableId = id)) // ui 层不提供 table_id，在这里补充。
        }
    }

    fun updateName(id: Long, name: String) {
        runner.launch {
            if (foodRepository.updateName(id, name) == 0) {
                throw FoodNotFoundException(id)
            }
        }
    }

    fun updateWeight(id: Long, weight: Int) {
        runner.launch {
            if (foodRepository.updateWeight(id, weight) == 0) {
                throw FoodNotFoundException(id)
            }
        }
    }

    fun updateMarked(id: Long, marked: Boolean) {
        runner.launch {
            if (foodRepository.updateMarked(id, marked) == 0) {
                throw FoodNotFoundException(id)
            }
        }
    }

    fun deleteFoodById(id: Long) {
        runner.launch(successMessage = "删除成功") {
            if (foodRepository.deleteById(id) == 0) throw FoodNotFoundException(id)
            selectionIdStateFlow.update { current ->
                if (current is FoodSelectionIdState.Chosen && current.id == id)
                    FoodSelectionIdState.Idle
                else
                    current
            }
        }
    }

    // 随机选择

    fun chosenRandomFood() {
        runner.launch {
            val tableId = foodPreferences.getFoodTableId() ?: throw NoTableSelectedException()
            foodTableRepository.findByIdOnce(tableId) ?: throw NoTableSelectedException()
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
    }

    fun ignoreChosenFood() {
        runner.launch(successMessage = "已忽略当前选择食物") {
            val selectionState = selectionIdStateFlow.value
            val id = (selectionState as? FoodSelectionIdState.Chosen)?.id ?: throw NoFoodSelectedException()

            val update = foodRepository.updateMarked(id, false)
            if (update == 0) throw FoodNotFoundException(id)
        }
    }

    fun recoverAll() {
        runner.launch(successMessage = "已恢复所有被忽略的食物") {
            val id = foodPreferences.getFoodTableId() ?: throw NoTableSelectedException()
            foodTableRepository.findByIdOnce(id) ?: throw NoTableSelectedException()
            foodRepository.updateAllMarked(id, marked = true)
        }
    }

    fun clearSelection() {
        runner.launch(successMessage = "已清除当前选择") {
            selectionIdStateFlow.value = FoodSelectionIdState.Idle
        }
    }



    // table
    private fun saveCurrentTableId(id: Long?) {
        foodPreferences.saveFoodTableId(id)
    }

    fun exportTable(
        onExport: suspend (String) -> Unit
    ) {
        runner.launch(successMessage = "已导出json至剪贴板") {
            val id = foodPreferences.getFoodTableId() ?: throw NoTableSelectedException()
            val table = foodTableRepository.findByIdOnce(id) ?: throw FoodTableNotFoundException(id)
            val foods = foodRepository.findByTableIdOnce(id)
            onExport(foodTableToJson(table, foods))
        }
    }

    fun importTable(json: String) {
        runner.launch(successMessage = "已导入表格") {

            val foodTableDto = try {
                jsonToFoodTableDto(json)
            } catch (_: SerializationException) {
                throw InvalidFoodTableJsonException()
            }

            val id = foodTableRepository.insert(FoodTable(name = foodTableDto.name))
            foodRepository.insertAll(foodTableDto.foodDtos.map {
                Food(name = it.name, weight = it.weight, marked = true, tableId = id)
            })
            if(foodPreferences.getFoodTableId() == null) {
                foodPreferences.saveFoodTableId(id)
            }
        }
    }


    fun switchTable(id: Long) {
        runner.launch {
            val preId = foodPreferences.getFoodTableId()
            if(id == preId) return@launch
            foodPreferences.saveFoodTableId(id)
            selectionIdStateFlow.value = FoodSelectionIdState.Idle
        }
    }

    fun createTable(name: String) {
        runner.launch {
            val id = foodTableRepository.insert(FoodTable(name = name))
            repeat(3) {
                foodRepository.insert(
                    Food(
                        name = "",
                        weight = 1,
                        marked = true,
                        tableId = id
                    )
                )
            }
            if(foodPreferences.getFoodTableId() == null) {
                foodPreferences.saveFoodTableId(id)
            }
        }
    }

    fun renameTable(name: String) {
        runner.launch {
            val id = foodPreferences.getFoodTableId() ?: throw NoTableSelectedException()
            foodTableRepository.findByIdOnce(id) ?: throw NoTableSelectedException()
            if(foodTableRepository.updateName(id, name) == 0) {
                throw FoodTableNotFoundException(id)
            }
        }
    }

    fun deleteTable() {
        runner.launch(successMessage = "已删除表格") {
            val id = foodPreferences.getFoodTableId() ?: throw NoTableSelectedException()
            foodTableRepository.findByIdOnce(id) ?: throw NoTableSelectedException()
            val tables = foodTableRepository.findAllOnce()

            val index = tables.indexOfFirst { it.id == id }
            if(index == -1) throw FoodTableNotFoundException(id)

            foodRepository.deleteByTableId(id)
            if(foodTableRepository.deleteById(id) == 0)
                throw FoodTableNotFoundException(id)

            val newId: Long? = when {
                index - 1 >= 0 -> tables[index - 1].id
                index + 1 < tables.size -> tables[index + 1].id
                else -> null
            }

            saveCurrentTableId(newId)
            selectionIdStateFlow.value = FoodSelectionIdState.Idle
        }
    }
}
