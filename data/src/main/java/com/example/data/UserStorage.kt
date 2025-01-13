package com.example.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.domain.UserStorageContract
import javax.inject.Inject

class UserStorage @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : UserStorageContract {

    override fun saveString(key: UserStorageContract.Key, data: String) {
        sharedPreferences.edit { putString(key.prefsValue, data) }
    }

    override fun getString(key: UserStorageContract.Key): String? {
        return sharedPreferences.getString(key.prefsValue, "")
    }

    override fun contains(key: UserStorageContract.Key): Boolean {
        return sharedPreferences.contains(key.prefsValue)
    }

    override fun clear() {
        sharedPreferences.edit { clear() }
    }
}