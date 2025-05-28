package com.example.feature_auth.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.viewModelScope
import com.example.base.AbstractMviViewModel
import com.example.domain.UserStorageContract
import com.example.domain.interactor.AuthInteractor
import com.example.domain.model.Email
import com.example.domain.model.PhoneNumber
import com.example.domain.model.User
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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
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
            Timber.tag("registration___________").d("intentSharedFlow")
            when (newIntent) {
                is RegistrationContract.ViewIntent.Registration -> {
                    Timber.tag("registration______________________").d("Registration")
                    registrationWithEmail(email.value)
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

    private fun registrationWithEmail(email: Email) = launch {
        _viewState.update { it.copy(isLoading = true) }
        val result = authInteractor.registerWithEmailAndPassword(email.value, email.password)
        result.onSuccess { user ->

            _viewState.update { it.copy(isLoading = false) }
            sendEvent(RegistrationContract.SingleEvent.NavigateToSignIn)

        }.onFailure { exception ->
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