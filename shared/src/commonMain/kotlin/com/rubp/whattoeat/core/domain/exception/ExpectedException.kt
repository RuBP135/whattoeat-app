package com.rubp.whattoeat.core.domain.exception

abstract class ExpectedException(
    override val message: String
) : Exception(message)
