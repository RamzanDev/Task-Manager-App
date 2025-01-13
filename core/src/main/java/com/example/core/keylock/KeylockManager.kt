package com.example.core.keylock

import com.example.domain.UserStorageContract

class KeylockManager (
    private val secureStorageContract: UserStorageContract
) {
    val isAuthenticated: Boolean
        get() = !secureStorageContract.getString(UserStorageContract.Key.KEY_ACCESS_TOKEN).isNullOrEmpty()

    private var deferredDestination: String? = null

    fun setDeferredDestination(destination: String) {
        deferredDestination = destination
    }

    fun getDeferredDestination(): String? {
        return deferredDestination
    }
}