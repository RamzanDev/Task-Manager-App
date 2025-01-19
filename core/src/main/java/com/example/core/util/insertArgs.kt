package com.example.core.util

fun String.insertArgs(name: String, value: String): String {
    return this.replace("{$name}", value)
}