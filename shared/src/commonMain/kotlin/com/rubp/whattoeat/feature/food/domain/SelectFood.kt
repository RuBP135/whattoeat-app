package com.rubp.whattoeat.domain

import com.rubp.whattoeat.feature.food.data.entity.Food

fun selectFood(
    foodList: List<Food>,
    chosenFood: Food?
): Food? {
    if (foodList.isEmpty()) return null

    val candidates = if (foodList.size == 1) {
        foodList
    } else {
        foodList.filter { food -> food != chosenFood }
    }

    val totalWeight = candidates.sumOf { food -> food.weight }
    val random = kotlin.random.Random.nextDouble()
    var sumWeight = 0

    for (food in candidates) {
        sumWeight += food.weight
        if (sumWeight.toDouble() / totalWeight >= random) {
            return food // 一定非null
        }
    }
    return candidates.last()
}