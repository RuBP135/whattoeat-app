package com.rubp.whattoeat.core.data.exception

class DatabaseReadException(
    val operation: String,
    cause: Throwable
) : RepositoryException(
    message = "数据库读取失败：$operation",
    cause = cause
)
