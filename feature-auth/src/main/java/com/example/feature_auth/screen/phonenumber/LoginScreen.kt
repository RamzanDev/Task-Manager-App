package com.example.feature_auth.screen.phonenumber

import android.app.Activity
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
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.navigation.BaseNavigator
import com.example.core.navigation.MockNavigator
import com.example.domain.model.PhoneNumber
import com.example.feature_auth.contract.LoginContract
import com.example.feature_auth.navigation.FeatureAuthRoute
import com.example.feature_auth.viewmodel.LoginViewModel
import com.example.uikit.theme.TaskManagerMargin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.TimeUnit


@Composable
fun PhoneNumberScreen(
    navigator: BaseNavigator = MockNavigator,
    viewModel: LoginViewModel = hiltViewModel(),
    activity: Activity
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
            is LoginContract.SingleEvent.NavigateToOtp -> {
                Timber.tag("navigate").d("NavigateToOtp")
                navigator.navigate(FeatureAuthRoute.Otp.create(event.phoneNumber.value))
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
                onPhoneNumberEntered = {
                    intentChannel.trySend(LoginContract.ViewIntent.OnNumberChanged(it))
                },
                onActionClicked = {
                    val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(activity)
                    intentChannel.trySend(LoginContract.ViewIntent.Login(options))
                    Timber.tag("login___________").d("trySend")

                }

            )
        }
        ShowNumberNotFound(isVisible = viewState.isNumberNotFoundVisible)

    }

}

@Composable
private fun InputContainer(
    modifier: Modifier = Modifier,
    viewState: LoginContract.ViewState,
    onPhoneNumberEntered: (PhoneNumber) -> Unit = {},
    onActionClicked: () -> Unit = {}
) {
    var phoneNumber by remember { mutableStateOf("") }
    var isPhoneValid by remember { mutableStateOf(true) }

    val validatePhoneNumber: (String) -> Boolean = { phone ->
        phone.matches(Regex("^\\+?[1-9]\\d{1,14}\$")) // Пример валидации для международного номера
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
            text = stringResource(com.example.locale.R.string.auth_enter_phone_number),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = {
                phoneNumber = it
                val phoneNumber = PhoneNumber.create(it)
                onPhoneNumberEntered.invoke(phoneNumber)
                isPhoneValid = validatePhoneNumber(it)

            },
            label = { Text(text = stringResource(com.example.locale.R.string.auth_phone_number)) },
            isError = !isPhoneValid,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (!isPhoneValid) {
            Text(
                text = stringResource(com.example.locale.R.string.auth_wrong_format_number),
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

            enabled = isPhoneValid && phoneNumber.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(com.example.locale.R.string.auth_continue))
        }
        Spacer(
            modifier =
            Modifier
                .size(TaskManagerMargin.S)
                .align(Alignment.CenterHorizontally),
        )
        Button(
            onClick = {
                // тут реализовать регистрацию
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(com.example.locale.R.string.auth_register_in))
        }
    }
}


@Composable
private fun ShowNumberNotFound(isVisible: Boolean) {
    if (isVisible) {
        PhoneNotFoundToast(message = stringResource(com.example.locale.R.string.auth_number_not_found))
    }
}

@Composable
fun PhoneNotFoundToast(message: String) {
    val context = LocalContext.current
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}