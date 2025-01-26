package com.example.feature_auth.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.base.AbstractMviViewModel
import com.example.domain.interactor.AuthInteractor
import com.example.domain.model.Email
import com.example.domain.model.PhoneNumber
import com.example.feature_auth.contract.LoginContract
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

    private val email = MutableStateFlow(Email.EMPTY)

    private val _viewState = MutableStateFlow(LoginContract.ViewState(email = email.value))

    override val viewState: StateFlow<LoginContract.ViewState> = _viewState.asStateFlow()

    init {
        email
            .filter { it.value.isNotEmpty() }
            .onEach { email ->
                _viewState.update {
                    it.copy(
                        email = email,
                        isEnable = email.isValidEmail()
                    )
                }
            }
            .stateIn(viewModelScope, SharingStarted.Eagerly, PhoneNumber.EMPTY)

        intentSharedFlow.onEach { newIntent ->
            Timber.tag("login___________").d("intentSharedFlow")
            when (newIntent) {
                is LoginContract.ViewIntent.Login -> {
                    loginWithEmail(email.value)
                }

                is LoginContract.ViewIntent.OnEmailChanged -> {
                    email.value = newIntent.email
                }

                is LoginContract.ViewIntent.SetEmailIsNotCorrect -> {
                    _viewState.update {
                        it.copy(isEmailNotCorrect = newIntent.isVisible)
                    }
                }

                is LoginContract.ViewIntent.Registration -> {
                    openRegistrationScreen()
                }
            }
        }.shareIn(this.viewModelScope, SharingStarted.Eagerly)
    }

    private fun loginWithEmail(email: Email) = launch {
        _viewState.update { it.copy(isLoading = true) }
        val result = authInteractor.loginWithEmail(email.value, email.password)
        result.onSuccess { user ->
            // Пользователь успешно вошёл
            _viewState.update { it.copy(isLoading = false) }
            sendEvent(LoginContract.SingleEvent.NavigateToMainScreen)
            println("Welcome ${user?.email}")
        }.onFailure { exception ->
            // Ошибка входа
            _viewState.update {
                it.copy(
                    isEmailNotCorrect = true,
                    isLoading = false
                )
            }
            println("Login failed: ${exception.message}")
        }

    }

    private fun openRegistrationScreen() {
        launch {
            sendEvent(LoginContract.SingleEvent.NavigateToRegistration)

        }
    }
}