package com.rubp.whattoeat.core.data.exception

abstract class RepositoryException(
    message: String,
    cause: Throwable
) : Exception(message, cause)
