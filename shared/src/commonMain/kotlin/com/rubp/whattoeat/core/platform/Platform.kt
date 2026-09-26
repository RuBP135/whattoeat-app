package com.rubp.whattoeat.core.platform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform