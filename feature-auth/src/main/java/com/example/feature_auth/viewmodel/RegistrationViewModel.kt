package com.example.feature_auth.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.base.AbstractMviViewModel
import com.example.domain.interactor.AuthInteractor
import com.example.domain.model.Email
import com.example.domain.model.PhoneNumber
import com.example.feature_auth.contract.RegistrationContract
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
class RegistrationViewModel @Inject constructor(
    private val authInteractor: AuthInteractor
) : AbstractMviViewModel<RegistrationContract.ViewIntent, RegistrationContract.ViewState, RegistrationContract.SingleEvent>() {


    private val email = MutableStateFlow(Email.EMPTY)

    private val _viewState = MutableStateFlow(RegistrationContract.ViewState(email = email.value))

    override val viewState: StateFlow<RegistrationContract.ViewState> = _viewState.asStateFlow()

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
                is RegistrationContract.ViewIntent.Registration -> {
                    RegistrationWithEmail(email.value)
                }

                is RegistrationContract.ViewIntent.OnEmailChanged -> {
                    email.value = newIntent.value
                }

                is RegistrationContract.ViewIntent.SetEmailIsNotCorrect -> {
                    _viewState.update {
                        it.copy(isEmailNotCorrect = newIntent.isVisible)
                    }
                }
            }
        }.shareIn(this.viewModelScope, SharingStarted.Eagerly)
    }

    private fun RegistrationWithEmail(email: Email) = launch {
        _viewState.update { it.copy(isLoading = true) }
        val result = authInteractor.registerWithEmailAndPassword(email.value, email.password)
        result.onSuccess { user ->
            // Пользователь успешно зарегистрирован
            _viewState.update { it.copy(isLoading = false) }
            sendEvent(RegistrationContract.SingleEvent.NavigateToSignIn)
            println("Welcome ${user?.email}")
        }.onFailure { exception ->
            // Ошибка регистрации
            _viewState.update {
                it.copy(
                    isEmailNotCorrect = true,
                    isLoading = false
                )
            }
            println("Login failed: ${exception.message}")
        }

    }
}