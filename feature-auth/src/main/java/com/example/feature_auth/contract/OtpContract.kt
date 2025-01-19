package com.example.feature_auth.contract

import android.net.Uri
import androidx.compose.runtime.Immutable
import com.example.base.MviIntent
import com.example.base.MviSingleEvent
import com.example.base.MviViewState
import com.example.domain.model.PhoneNumber

interface OtpContract {
    @Immutable
    sealed interface ViewIntent : MviIntent {
        data class SetupPhoneNumber(val phoneNumber: PhoneNumber) : ViewIntent
        data class OnOtpEntered(val value: String) : ViewIntent
        data object OnResendClicked : ViewIntent
    }


    data class ViewState(
        val timeLeft: Int = 0,
        val isErrorVisible: Boolean = false,
        val isLoading: Boolean = false
    ) : MviViewState

    sealed interface SingleEvent : MviSingleEvent {
        data class NavigateToMain(val deferredRoute: Uri? = null) : SingleEvent
        data class ShowError(val e: Throwable) : SingleEvent
    }
}