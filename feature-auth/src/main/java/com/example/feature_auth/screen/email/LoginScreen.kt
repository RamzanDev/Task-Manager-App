package com.example.feature_auth.screen.email

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.navOptions
import com.example.core.navigation.BaseNavigator
import com.example.core.navigation.MockNavigator
import com.example.core.navigation.NavigationGraphName
import com.example.domain.model.Email
import com.example.feature_auth.contract.LoginContract
import com.example.feature_auth.navigation.FeatureAuthRoute
import com.example.feature_auth.viewmodel.LoginViewModel
import com.example.uikit.theme.TaskManagerMargin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import timber.log.Timber


@Composable
fun SignInWithEmailScreen(
    navigator: BaseNavigator = MockNavigator,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val intentChannel = remember { Channel<LoginContract.ViewIntent>(Channel.UNLIMITED) }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Main.immediate) {
            intentChannel
                .consumeAsFlow()
                .onEach(viewModel::processIntent)
                .collect()
        }
    }

    val viewEvent by viewModel.singleEvent.collectAsStateWithLifecycle(initialValue = null)
    LaunchedEffect(key1 = viewEvent) {
        when (val event = viewEvent) {
            is LoginContract.SingleEvent.NavigateToMainScreen -> {
                Timber.tag("navigate").d("NavigateToMainScreen")
                navigator.navigate(
                    route = NavigationGraphName.FEATURE_MAIN.id,
                    navOptions =
                    navOptions {
                        popUpTo(NavigationGraphName.FEATURE_MAIN.id) {
                            inclusive = false
                        }
                    },
                )
            }

            is LoginContract.SingleEvent.NavigateToRegistration -> {
                Timber.tag("navigate").d("NavigateToRegistrationScreen")
                navigator.navigate(FeatureAuthRoute.Registration.INSTANCE)
            }

            null -> Unit
        }
    }

    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    Scaffold { paddings ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
        ) {
            val (container, icons) = createRefs()
            InputContainer(
                modifier = Modifier.constrainAs(container) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.wrapContent
                    bottom.linkTo(icons.top)
                },
                viewState = viewState,
                onEmailEntered = {
                    intentChannel.trySend(LoginContract.ViewIntent.OnEmailChanged(it))
                },
                onActionClicked = {
                    intentChannel.trySend(LoginContract.ViewIntent.Login)
                    Timber.tag("login___________").d("trySend")
                },
                onRegistrationClicked = {
                    intentChannel.trySend(LoginContract.ViewIntent.Registration)
                }
            )
        }
        ShowEmailNotFound(isVisible = viewState.isEmailNotCorrect)
    }

}

@Composable
private fun InputContainer(
    modifier: Modifier = Modifier,
    viewState: LoginContract.ViewState,
    onEmailEntered: (Email) -> Unit = {},
    onActionClicked: () -> Unit = {},
    onRegistrationClicked: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }

    val validateEmail: (String) -> Boolean = { email ->
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        email.matches(emailRegex)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (viewState.isLoading) {
            Column {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }

        Text(
            text = stringResource(com.example.locale.R.string.auth_enter_email_and_password),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                isEmailValid = validateEmail(it)
                onEmailEntered(Email(email, password))
            },
            label = { Text(text = stringResource(com.example.locale.R.string.auth_email)) },
            isError = !isEmailValid,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (!isEmailValid) {
            Text(
                text = stringResource(com.example.locale.R.string.auth_invalid_email),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                isPasswordValid = password.isNotEmpty() && password.length >= 6
                onEmailEntered(Email(email, password))
            },
            label = { Text(text = stringResource(com.example.locale.R.string.auth_password)) },
            isError = !isPasswordValid,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (!isPasswordValid) {
            Text(
                text = stringResource(com.example.locale.R.string.auth_invalid_password),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onActionClicked()
            },
            enabled = isEmailValid && isPasswordValid && email.isNotBlank() && password.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(com.example.locale.R.string.auth_continue))
        }

        Spacer(
            modifier = Modifier
                .size(TaskManagerMargin.S)
                .align(Alignment.CenterHorizontally),
        )

        Button(
            onClick = {
                onRegistrationClicked()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(com.example.locale.R.string.auth_register_in))
        }
    }
}


@Composable
private fun ShowEmailNotFound(isVisible: Boolean) {
    if (isVisible) {
        EmailNotFoundToast(message = stringResource(com.example.locale.R.string.auth_email_is_not_found))
    }
}

@Composable
fun EmailNotFoundToast(message: String) {
    val context = LocalContext.current
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}