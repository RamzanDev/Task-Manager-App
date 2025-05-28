package com.example.feature_auth.contract

import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.Immutable
import com.example.base.MviIntent
import com.example.base.MviSingleEvent
import com.example.base.MviViewState
import com.example.domain.model.Email

interface LoginContract {

    @Immutable
    sealed interface ViewIntent : MviIntent {
        data class GoogleSignIn(val launcher: ManagedActivityResultLauncher<Intent, ActivityResult>) : ViewIntent
        data object GoogleSignOut : ViewIntent
    }

    data class ViewState(
        val idle: Boolean = true, // Отображает, что на экране ничего не происходит
        val isSigningIn: Boolean = false, // Статус аутентификации
        val isSignedIn: Boolean = false, // Успешный вход
        val idToken: String? = null, // Токен после успешного входа
        val hasSingInError: Boolean = false, // Сообщение об ошибке
        val signInErrorMessage: String = "", // Сообщение об ошибке
    ) : MviViewState

    sealed interface SingleEvent : MviSingleEvent {
        data object NavigateToMainScreen : SingleEvent
    }
}