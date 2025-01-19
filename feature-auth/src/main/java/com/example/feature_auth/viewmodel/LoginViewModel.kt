package com.example.feature_auth.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.base.AbstractMviViewModel
import com.example.domain.interactor.AuthInteractor
import com.example.domain.model.PhoneNumber
import com.example.feature_auth.contract.LoginContract
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authInteractor: AuthInteractor
) : AbstractMviViewModel<LoginContract.ViewIntent, LoginContract.ViewState, LoginContract.SingleEvent>() {

    private val phoneNumber = MutableStateFlow(PhoneNumber.EMPTY)

    private val _viewState =
        MutableStateFlow(LoginContract.ViewState(phoneValue = phoneNumber.value))

    override val viewState: StateFlow<LoginContract.ViewState> = _viewState.asStateFlow()

    private fun createFirebaseAuthCallbacks(
        onResult: (Result<String>) -> Unit
    ): PhoneAuthProvider.OnVerificationStateChangedCallbacks {
        return object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                onResult(Result.success("Verification successful"))
            }

            override fun onVerificationFailed(exception: FirebaseException) {
                Timber.e("Ошибка верификации: ${exception.message}")
                val errorMessage = when (exception) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        Timber.e("Неверный формат номера телефона.")
                        "Invalid phone number format"
                    }

                    is FirebaseTooManyRequestsException -> {
                        Timber.e("Достигнут лимит запросов. Попробуйте позже.")
                        "Too many requests. Please try again later."
                    }

                    else -> {
                        Timber.e("Неизвестная ошибка: $exception")
                        "Unknown error: ${exception.message}"
                    }
                }
                onResult(Result.failure(Exception(errorMessage)))
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Timber.tag("____").d("Verification code sent")
                onResult(Result.success("Verification code sent with ID: $verificationId"))
            }
        }
    }

    init {
        phoneNumber
            .filter { it.value.isNotEmpty() }
            .onEach { phoneNumber ->
                _viewState.update {
                    it.copy(
                        phoneValue = phoneNumber,
                        isEnable = phoneNumber.isValid()
                    )
                }
            }
            .stateIn(viewModelScope, SharingStarted.Eagerly, PhoneNumber.EMPTY)

        intentSharedFlow.onEach { newIntent ->
            Timber.tag("login___________").d("intentSharedFlow")
            when (newIntent) {
                is LoginContract.ViewIntent.Login -> {
                    val options = newIntent.options
                        .setPhoneNumber(phoneNumber.value.value)
                        .setCallbacks(createFirebaseAuthCallbacks { navigateToOtpScreen(it) })
                        .build()

                    login(options)
                }

                is LoginContract.ViewIntent.OnNumberChanged -> {
                    phoneNumber.value = newIntent.phoneNumber
                }

                is LoginContract.ViewIntent.SetNumberNotFoundVisibility -> {
                    _viewState.update {
                        it.copy(isNumberNotFoundVisible = newIntent.isVisible)
                    }
                }
            }
        }.shareIn(this.viewModelScope, SharingStarted.Eagerly)
    }

    private fun login(options: PhoneAuthOptions) = launch {
        _viewState.update { it.copy(isLoading = true) }
        authInteractor.login(phoneNumber.value, options)
    }

    private fun navigateToOtpScreen(result: Result<String>) {
        result.onSuccess {
            Timber.tag("login___________").d("onSuccess")
            viewModelScope.launch {
                _viewState.update { it.copy(isLoading = false) }
                sendEvent(LoginContract.SingleEvent.NavigateToOtp(phoneNumber = phoneNumber.value))
                Timber.tag("navigate").d("NavigateToOtp")

            }
        }.onFailure {
            Timber.tag("login___________").d("onFailure")
            _viewState.update {
                it.copy(
                    isNumberNotFoundVisible = true,
                    isLoading = false
                )
            }
        }
    }
}