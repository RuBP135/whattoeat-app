package com.rubp.whattoeat.feature.food.domain.exception

import com.rubp.whattoeat.core.domain.exception.BusinessException

class NoTableSelectedException : BusinessException("请先选择一个表格")
