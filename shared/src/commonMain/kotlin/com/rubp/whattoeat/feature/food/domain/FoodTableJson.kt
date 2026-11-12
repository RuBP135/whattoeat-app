package com.rubp.whattoeat.feature.food.domain

import com.rubp.whattoeat.feature.food.data.entity.Food
import com.rubp.whattoeat.feature.food.data.entity.FoodTable
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Serializable
data class FoodDto(val name: String, val weight: Int) // createdAt和tableId，以及marked需要其他地方分配
@Serializable
data class FoodTableDto(val name: String, val foodDtos:List<FoodDto>) // createdAt和tableId需要其他地方分配

fun Food.toFoodDto(): FoodDto {
    return FoodDto(this.name, this.weight)
}

fun foodTableToJson(foodTable: FoodTable, foods: List<Food>): String {
    val foodDtos = foods.map { it.toFoodDto()}
    val fooTableDto = FoodTableDto(foodTable.name, foodDtos)
    return Json.encodeToString(fooTableDto)
}

// 会抛出异常
@Throws(SerializationException::class, IllegalArgumentException::class)
fun jsonToFoodTableDto(jsonStr: String): FoodTableDto {
    return Json.decodeFromString<FoodTableDto>(jsonStr)
}