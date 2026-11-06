package com.rubp.whattoeat.feature.food.domain.exception

import com.rubp.whattoeat.core.domain.exception.ExpectedException

class FoodNotFoundException(
    val id: Long
) : ExpectedException("该食物已不存在")
