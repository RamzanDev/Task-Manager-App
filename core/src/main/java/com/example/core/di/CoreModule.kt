package com.example.core.di

import com.example.core.keylock.KeylockManager
import com.example.domain.UserStorageContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import poster.pai.core.dispatcher.AppCoroutineDispatchers
import poster.pai.core.dispatcher.DefaultCoroutineDispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object CoreModule {

    @Provides
    @Singleton
    fun provideCoroutineDispatchers(): AppCoroutineDispatchers = DefaultCoroutineDispatchers()

    @Provides
    @Singleton
    fun provideKeyLockManager(userStorageContract: UserStorageContract): KeylockManager =
        KeylockManager(userStorageContract)
}