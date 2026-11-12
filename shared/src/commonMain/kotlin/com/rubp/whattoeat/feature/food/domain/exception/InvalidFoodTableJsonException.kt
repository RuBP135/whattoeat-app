package com.rubp.whattoeat.feature.food.domain.exception

import com.rubp.whattoeat.core.domain.exception.BusinessException

class InvalidFoodTableJsonException : BusinessException("json格式错误")
