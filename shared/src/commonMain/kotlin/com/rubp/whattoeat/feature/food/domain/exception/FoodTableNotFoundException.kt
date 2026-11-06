package com.rubp.whattoeat.feature.food.domain.exception

import com.rubp.whattoeat.core.domain.exception.ExpectedException

class FoodTableNotFoundException(
    val id: Long
) : ExpectedException("该表格已不存在")
