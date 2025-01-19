package com.example.feature_auth.contract

import androidx.compose.runtime.Immutable
import com.example.base.MviIntent
import com.example.base.MviSingleEvent
import com.example.base.MviViewState
import com.example.domain.model.PhoneNumber
import com.google.firebase.auth.PhoneAuthOptions

interface LoginContract {

    @Immutable
    sealed interface ViewIntent : MviIntent {
        data class Login(val options: PhoneAuthOptions.Builder) : ViewIntent
        data class OnNumberChanged(val phoneNumber: PhoneNumber) : ViewIntent
        data class SetNumberNotFoundVisibility(val isVisible: Boolean = false) : ViewIntent
    }

    data class ViewState(
        val isLoading: Boolean = false,
        val isEnable: Boolean = false,
        val phoneValue: PhoneNumber = PhoneNumber.EMPTY,
        val isNumberNotFoundVisible: Boolean = false
    ) : MviViewState

    sealed interface SingleEvent : MviSingleEvent {
        data class NavigateToOtp(val phoneNumber: PhoneNumber) : SingleEvent
    }
}