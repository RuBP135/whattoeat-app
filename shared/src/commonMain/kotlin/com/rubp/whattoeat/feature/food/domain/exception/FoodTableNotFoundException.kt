package com.rubp.whattoeat.feature.food.domain.exception

import com.rubp.whattoeat.core.domain.exception.BusinessException

class FoodTableNotFoundException(
    val id: Long
) : BusinessException("该表格已不存在")
