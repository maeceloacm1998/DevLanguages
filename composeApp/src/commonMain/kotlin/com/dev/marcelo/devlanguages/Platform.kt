package com.dev.marcelo.devlanguages

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform