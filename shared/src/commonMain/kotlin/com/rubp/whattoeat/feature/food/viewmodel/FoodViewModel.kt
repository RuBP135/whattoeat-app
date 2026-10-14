package com.rubp.whattoeat.feature.food.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubp.whattoeat.feature.food.data.entity.Food
import com.rubp.whattoeat.feature.food.data.entity.FoodTable
import com.rubp.whattoeat.feature.food.data.preferences.FoodPreferences
import com.rubp.whattoeat.feature.food.data.repository.FoodRepository
import com.rubp.whattoeat.feature.food.data.repository.FoodTableRepository
import com.rubp.whattoeat.feature.food.domain.exception.FoodNotFoundException
import com.rubp.whattoeat.feature.food.domain.exception.NoFoodSelectedException
import com.rubp.whattoeat.feature.food.domain.exception.NoTableSelectedException
import com.rubp.whattoeat.feature.food.domain.foodTableToJson
import com.rubp.whattoeat.feature.food.domain.jsonToFoodTableDto
import com.rubp.whattoeat.feature.food.domain.selectFood
import com.rubp.whattoeat.feature.food.ui.choose.FoodSelectionState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class FoodViewModel(
    private val foodRepository: FoodRepository,
    private val foodTableRepository: FoodTableRepository,
    private val foodPreferences: FoodPreferences
) : ViewModel() {

    init {
        viewModelScope.launch {
            if(foodTableRepository.findAll().first().isEmpty()){
                createTable("默认")
            }
        }
    }
    val tableListStateFlow: StateFlow<List<FoodTable>> = foodTableRepository.findAll()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val currentTableStateFlow: StateFlow<FoodTable?> =
        foodPreferences.foodTableIdFlow.flatMapLatest { tableId ->
            tableId?.let {
                foodTableRepository.findById(tableId)
            } ?: flowOf(null)
    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
    )

    val foodListStateFlow: StateFlow<List<Food>> = currentTableStateFlow.flatMapLatest { table ->
        table?.let{
            foodRepository.findByTableId(table.id)
        } ?: flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _selectionStateFlow: MutableStateFlow<FoodSelectionState> = MutableStateFlow(FoodSelectionState.Idle)
    val selectionStateFlow: StateFlow<FoodSelectionState> = _selectionStateFlow.asStateFlow()

    val chosenFoodStateFlow: StateFlow<Food?> =
        combine(selectionStateFlow, foodListStateFlow) { selectionState, foodList ->
            when(selectionState) {
                is FoodSelectionState.Chosen -> foodList.find { it.id == selectionState.id }
                else -> null
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )




    // 添加新菜品用，没有选择表格的时候不会添加
    suspend fun insert(food: Food) {
        // ui 层不提供 table_id，在这里补充。
        currentTableStateFlow.value?.let { table ->
            foodRepository.insert(food.copy(tableId = table.id))
        }
    }

    suspend fun updateName(id: Long, name: String) {
        foodRepository.updateName(id, name)
    }

    suspend fun updateWeight(id: Long, weight: Int) {
        foodRepository.updateWeight(id, weight)
    }

    suspend fun updateMarked(id: Long, marked: Boolean) {
        foodRepository.updateMarked(id, marked)
    }

    suspend fun deleteById(id: Long) {
        val state = selectionStateFlow.value
        if(state is FoodSelectionState.Chosen){
            if(state.id == id){
                _selectionStateFlow.value = FoodSelectionState.Idle
            }
        }
        foodRepository.deleteById(id)
    }

    // 随机选择

    fun chosenRandomFood() {
        val foodList = foodListStateFlow.value.filter { food ->
            food.marked && food.weight > 0 && food.name.isNotEmpty()
        }

        val preId = (_selectionStateFlow.value as? FoodSelectionState.Chosen)?.id
        val id = selectFood(foodList, preId);

        _selectionStateFlow.value = id?.let { FoodSelectionState.Chosen(it)} ?: FoodSelectionState.NoAvailableFood
    }

    suspend fun ignoreChosenFood() {
        val state = _selectionStateFlow.value
        if (state !is FoodSelectionState.Chosen) {
            throw NoFoodSelectedException()
        }
        val updatedRows = foodRepository.updateMarked(state.id, false)
        if (updatedRows == 0) {
            throw FoodNotFoundException(state.id)
        }
    }

    suspend fun clearAllIgnore() {
        val table = currentTableStateFlow.value ?: throw NoTableSelectedException()
        foodRepository.updateAllMarked(table.id, marked = true)
    }

    fun clearSelection() {
        _selectionStateFlow.value = FoodSelectionState.Idle
    }





    // 表格管理
    private fun saveCurrentTableId(id: Long){
        foodPreferences.saveFoodTableId(id)
    }

    fun exportTable(): String {
        val table = currentTableStateFlow.value ?: throw NoTableSelectedException()
        return foodTableToJson(table, foodListStateFlow.value)
    }

    suspend fun importTable(jsonStr: String) {
        val foodTableDto = jsonToFoodTableDto(jsonStr)
        val wasEmpty = tableListStateFlow.value.isEmpty()
        val tableId = foodTableRepository.insert(FoodTable(name = foodTableDto.name))
        if(wasEmpty) switchTable(tableId) // 没有表格的时候，应该切换到这个新导入的表格
        foodRepository.insertAll(foodTableDto.foodDtos.map {
            Food(name = it.name, weight = it.weight, marked = true, tableId = tableId)
        })
    }


    fun switchTable(id: Long) {
        if (id == currentTableStateFlow.value?.id) return
        saveCurrentTableId(id)
        _selectionStateFlow.value = FoodSelectionState.Idle
    }

    suspend fun createTable(name: String) {
        val wasEmpty = tableListStateFlow.value.isEmpty()
        val newId = foodTableRepository.insert(FoodTable(name = name))
        if(wasEmpty) switchTable(newId) // 没有表格的时候，应该切换到这个新建的表格
        repeat(3) {
            foodRepository.insert(
                Food(name = "", weight = 1, marked = true, tableId = newId)
            )
        }
    }

    suspend fun renameTable(id: Long, newName: String) {
        if (currentTableStateFlow.value == null) {
            throw NoTableSelectedException()
        }
        val target = tableListStateFlow.value.find { it.id == id } ?: return
        foodTableRepository.update(target.copy(name = newName))
    }


    suspend fun deleteTable(id: Long) {
        if (currentTableStateFlow.value == null) {
            throw NoTableSelectedException()
        }

        val deletingCurrentTable = currentTableStateFlow.value?.id == id
        val state = selectionStateFlow.value

        if (deletingCurrentTable) {
            // 切换到剩余的表中

            val list = tableListStateFlow.value
            val index = list.indexOf(currentTableStateFlow.value)

            val newId: Long? = when {
                index != -1 && index - 1 >= 0 -> list.get(index - 1).id
                index != -1 && index + 1 < list.size -> list.get(index + 1).id
                else -> null
            }

            if(newId != null){
                saveCurrentTableId(newId)
            }

        }
        foodRepository.deleteByTableId(id)
        foodTableRepository.deleteById(id)

        if (deletingCurrentTable && selectionStateFlow.value == state) {
            clearSelection()
        }
    }
}
