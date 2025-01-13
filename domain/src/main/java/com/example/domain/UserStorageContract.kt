package com.example.domain

interface UserStorageContract {

    enum class Key(
        private val prefsKey: String,
        private var customKey: String? = null
    ) {
        KEY_ACCESS_TOKEN("KEY_ACCESS_TOKEN"),
        KEY_REFRESH_TOKEN("KEY_REFRESH_TOKEN");

        val prefsValue
            get() = customValue?.let {
                "${prefsKey}_$customValue"
            } ?: prefsKey

        private val customValue
            get() = customKey
    }

    fun saveString(key: Key, data: String)
    fun getString(key: Key): String?
    fun contains(key: Key): Boolean
    fun clear()
}