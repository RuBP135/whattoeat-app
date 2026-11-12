package com.rubp.whattoeat.feature.food.domain.exception

import com.rubp.whattoeat.core.domain.exception.BusinessException

class NoFoodSelectedException : BusinessException("请先选择一个食物")
