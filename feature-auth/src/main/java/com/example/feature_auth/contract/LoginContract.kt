package com.example.feature_auth.contract

import androidx.compose.runtime.Immutable
import com.example.base.MviIntent
import com.example.base.MviSingleEvent
import com.example.base.MviViewState
import com.example.domain.model.Email

interface LoginContract {

    @Immutable
    sealed interface ViewIntent : MviIntent {
        data object Login : ViewIntent
        data class OnEmailChanged(val email: Email) : ViewIntent
        data class SetEmailIsNotCorrect(val isVisible: Boolean = false) : ViewIntent
        data object Registration : ViewIntent
    }

    data class ViewState(
        val isLoading: Boolean = false,
        val isEnable: Boolean = false,
        val email: Email = Email.EMPTY,
        val isEmailNotCorrect: Boolean = false
    ) : MviViewState

    sealed interface SingleEvent : MviSingleEvent {
        data object NavigateToMainScreen : SingleEvent
        data object NavigateToRegistration : SingleEvent
    }
}