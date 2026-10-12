package com.rubp.whattoeat.core.data.exception

class DatabaseWriteException(
    val operation: String,
    cause: Throwable
) : RepositoryException(
    message = "数据库写入失败：$operation",
    cause = cause
)
