package com.rubp.whattoeat.feature.food.domain

import com.rubp.whattoeat.feature.food.data.entity.Food
import kotlin.random.Random

fun selectFood(
    foodList: List<Food>,
    chosenId: Long?
): Long? {
    if (foodList.isEmpty()) return null

    val candidates = if (foodList.size == 1) {
        foodList
    } else {
        foodList.filter { food -> food.id != chosenId }
    }

    val totalWeight = candidates.sumOf { food -> food.weight }
    val random = Random.nextDouble()
    var sumWeight = 0

    for (food in candidates) {
        sumWeight += food.weight
        if (sumWeight.toDouble() / totalWeight >= random) {
            return food.id // 一定非null
        }
    }
    return candidates.last().id
}