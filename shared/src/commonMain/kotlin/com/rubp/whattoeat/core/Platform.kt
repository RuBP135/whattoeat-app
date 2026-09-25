package com.rubp.whattoeat.core

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform