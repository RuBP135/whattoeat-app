package com.rubp.whattoeat.feature.food.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubp.whattoeat.feature.food.data.entity.Food
import com.rubp.whattoeat.feature.food.data.entity.FoodTable
import com.rubp.whattoeat.feature.food.data.preferences.FoodPreferences
import com.rubp.whattoeat.feature.food.data.repository.FoodRepository
import com.rubp.whattoeat.feature.food.data.repository.FoodTableRepository
import com.rubp.whattoeat.feature.food.domain.FoodTableDto
import com.rubp.whattoeat.feature.food.domain.selectFood
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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


    val tables: StateFlow<List<FoodTable>> = foodTableRepository.getAll()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    val currentTable: StateFlow<FoodTable?> =
        foodPreferences.foodTableIdFlow.flatMapLatest { tableId ->
            tableId?.let {
                foodTableRepository.getById(tableId)
            } ?: flowOf(null)
    }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
    )


    val foods: StateFlow<List<Food>> = currentTable.flatMapLatest { table ->
        table?.let{
            foodRepository.getByTableId(table.id)
        } ?: flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    var chosenFood: Food? = null

    init {
        // 首次启动无表时自动创建"默认"表格
        viewModelScope.launch {
            if(foodTableRepository.getAll().first().isEmpty()){
                createTable("默认")
            }
        }
    }

    // 表格管理

    fun switchTable(id: Long) {
        if (id == currentTable.value?.id) return
        saveCurrentTableId(id)
        chosenFood = null
    }

    fun createTable(name: String) {
        viewModelScope.launch {
            val wasEmpty = tables.value.isEmpty()
            val newId = foodTableRepository.insert(FoodTable(name = name))
            if(wasEmpty) switchTable(newId) // 没有表格的时候，应该切换到这个新建的表格
            repeat(3) {
                foodRepository.insert(
                    Food(name = "", weight = 1, marked = true, tableId = newId)
                )
            }
        }
    }

    fun renameTable(id: Long, newName: String) {
        viewModelScope.launch {
            val target = tables.value.find { it.id == id } ?: return@launch
            foodTableRepository.update(target.copy(name = newName))
        }
    }


    fun deleteTable(id: Long) {
        viewModelScope.launch {
            if (currentTable.value?.id == id) {
                // 切换到剩余的表中
                val remaining = tables.value.filter { it.id != id }
                if (remaining.isNotEmpty()) {
                    saveCurrentTableId(remaining.first().id)
                }
            }
            foodRepository.deleteByTableId(id)
            foodTableRepository.deleteById(id)
        }
    }

    // 添加新菜品用，没有选择表格的时候不会添加
    fun insert(food: Food) {
        viewModelScope.launch { // ui层不管之table_id，因此移到这里添加
            currentTable.value?.let { table ->
                foodRepository.insert(food.copy(tableId = table.id))
            }
        }
    }

    fun update(food: Food) {
        viewModelScope.launch {
            foodRepository.update(food)
        }
    }

    fun delete(food: Food) {
        viewModelScope.launch {
            foodRepository.delete(food)
        }
    }

    // 随机选择

    fun chosenRandomFood(): String {
        val foodList = foods.value.filter { food ->
            food.marked && food.weight > 0 && food.name.isNotEmpty()
        }

        chosenFood = selectFood(foodList, chosenFood)
        return chosenFood?.name ?: "没有可供选择的食物！"
    }

    fun ignoreChosenFood() {
        chosenFood?.let {
            update(it.copy(marked = false))
        }
    }

    fun clearAllIgnore() {
        viewModelScope.launch {
            currentTable.value?.let { table ->
                foodRepository.updateAllMarked(table.id, marked = true)
            }

        }
    }
    private fun saveCurrentTableId(id: Long){
        foodPreferences.saveFoodTableId(id)
    }

    fun inputFoodTableDto(foodTableDto: FoodTableDto){
        viewModelScope.launch {
            val wasEmpty = tables.value.isEmpty()
            val tableId = foodTableRepository.insert(FoodTable(name = foodTableDto.name))
            if(wasEmpty) switchTable(tableId) // 没有表格的时候，应该切换到这个新导入的表格
            foodRepository.insertAll(foodTableDto.foodDtos.map {
                Food(name = it.name, weight = it.weight, marked = true, tableId = tableId)
            })
        }
    }
}