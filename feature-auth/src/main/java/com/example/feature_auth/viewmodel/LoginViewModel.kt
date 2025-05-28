package com.example.feature_auth.viewmodel

import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.lifecycle.viewModelScope
import com.example.base.AbstractMviViewModel
import com.example.domain.UserStorageContract
import com.example.domain.interactor.AuthInteractor
import com.example.domain.model.User
import com.example.feature_auth.contract.LoginContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val userStorage: UserStorageContract,
) : AbstractMviViewModel<LoginContract.ViewIntent, LoginContract.ViewState, LoginContract.SingleEvent>() {


    private val _viewState = MutableStateFlow(LoginContract.ViewState())

    override val viewState: StateFlow<LoginContract.ViewState> = _viewState.asStateFlow()

    private var googleSignInClient: GoogleSignInClient? = null


    init {
        intentSharedFlow.onEach { newIntent ->
            when (newIntent) {
                is LoginContract.ViewIntent.GoogleSignIn -> {
                    startGoogleSignIn(newIntent.launcher)
                    Timber.tag("launcher_____").e("startGoogleSignIn")
                }

                is LoginContract.ViewIntent.GoogleSignOut -> signOut()

            }

        }.shareIn(this.viewModelScope, SharingStarted.Eagerly)

    }

    // Инициализация GoogleSignInOptions
    fun initializeGoogleSignIn(context: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("228049559657-c6osgn6e21524jelccafj89fs7uusfel.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun handleGoogleSignInResult(result: ActivityResult) {
        launch {
            Timber.tag("launcher_____").e("result code : ${result.resultCode}")
            try {
                Timber.tag("launcher_____").e("try")
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

                // Если task пустой или ошибка при извлечении аккаунта
                if (task.isSuccessful) {
                    val account = task.result
                    val idToken = account?.idToken

                    // Успешный вход
                    _viewState.update { it.copy(isSignedIn = true, idToken = idToken) }
                    userStorage.saveString(UserStorageContract.Key.KEY_ACCESS_TOKEN, idToken ?: "")
                    Timber.tag("User______________").d("auth success")
                    val newUser = User(
                        id = account?.id.orEmpty(),
                        email = account?.email.orEmpty(),
                        name = account?.displayName.orEmpty(),
                        photoUrl = account?.photoUrl.toString()
                    )
                    try {
                        val userJson = Json.encodeToString(newUser)
                        userStorage.saveString(UserStorageContract.Key.KEY_USER, userJson)
                        Timber.tag("User______________").d("User JSON saved: $userJson")
                    } catch (e: Exception) {
                        Timber.tag("User______________").e(e, "Error while saving user JSON")
                    }
                    Timber.tag("User______________").d("${userStorage.getString(UserStorageContract.Key.KEY_USER)}")
                    sendEvent(LoginContract.SingleEvent.NavigateToMainScreen)
                } else {
                    val e = task.exception
                        ?: ApiException(Status.RESULT_INTERNAL_ERROR)
                    Timber.tag("launcher_____").e("Sign-in failed: ${e.message}")
                    _viewState.update { state -> state.copy(signInErrorMessage = "Sign-in failed: ${e.message}") }
                }

            } catch (e: Exception) {
                // Ловим исключения и выводим сообщения
                Timber.tag("launcher_____").e("Exception: ${e.message}")
                _viewState.update { state -> state.copy(signInErrorMessage = "An unexpected error occurred") }
            }
        }

    }

    private fun startGoogleSignIn(launcher: ManagedActivityResultLauncher<Intent, ActivityResult>) {
        launch {
            val signInIntent = googleSignInClient?.signInIntent
            signInIntent?.let {
                launcher.launch(it) // Запуск через launcher
                Timber.tag("launcher_____").e("launcher.launch(it)")
                _viewState.update { state -> state.copy(hasSingInError = false) }
            }
        }
    }

    private fun signOut() {
        launch {
            val signInIntent = googleSignInClient
            signInIntent?.let {
                signInIntent.signOut()  // Запуск через launcher
                Timber.tag("launcher_____").e("signOut is done")

            }
        }
    }
}