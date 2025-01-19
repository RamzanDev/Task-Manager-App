package com.example.data.di

import android.content.Context
import android.content.SharedPreferences
import com.example.data.UserStorage
import com.example.domain.UserStorageContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("manager_app_preferences", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideUserStorage(sharedPreferences: SharedPreferences): UserStorageContract {
        return UserStorage(sharedPreferences)
    }
}