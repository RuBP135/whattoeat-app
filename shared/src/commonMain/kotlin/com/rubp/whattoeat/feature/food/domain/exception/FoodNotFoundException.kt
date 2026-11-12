package com.rubp.whattoeat.feature.food.domain.exception

import com.rubp.whattoeat.core.domain.exception.BusinessException

class FoodNotFoundException(
    val id: Long
) : BusinessException("该食物已不存在")
