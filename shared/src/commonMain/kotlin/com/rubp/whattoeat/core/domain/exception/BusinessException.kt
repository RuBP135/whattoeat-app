package com.rubp.whattoeat.core.domain.exception

abstract class BusinessException(
    override val message: String
) : Exception(message)
