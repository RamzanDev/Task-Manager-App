package com.example.taskmanagerapp

import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class TaskManagerApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        Timber.plant(Timber.DebugTree())
    }
}