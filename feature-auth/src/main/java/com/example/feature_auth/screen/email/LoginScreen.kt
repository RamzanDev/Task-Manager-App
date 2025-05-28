package com.example.feature_auth.screen.email

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.navOptions
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.core.navigation.BaseNavigator
import com.example.core.navigation.MockNavigator
import com.example.core.navigation.NavigationGraphName
import com.example.feature_auth.R
import com.example.feature_auth.contract.LoginContract
import com.example.feature_auth.viewmodel.LoginViewModel
import com.example.uikit.theme.ColorTheme
import com.example.uikit.theme.TextStyles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import timber.log.Timber


@Preview
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



            null -> Unit
        }
    }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.initializeGoogleSignIn(context)
    }
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Timber.tag("launcher_____").e("launcher result")
        viewModel.handleGoogleSignInResult(result)
    }

    Scaffold { paddings ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
        ) {
            val (container, icons) = createRefs()

            WelcomeContainer(
                modifier = Modifier.constrainAs(container) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.wrapContent
                    bottom.linkTo(icons.top)
                },
                onSingInClick = {

                    intentChannel.trySend(LoginContract.ViewIntent.GoogleSignIn(launcher))
                    Timber.tag("launcher_____").e("onSingInClick")

                },
                onSigOutClick = {
                    intentChannel.trySend(LoginContract.ViewIntent.GoogleSignOut)
                }

            )
        }
        ShowEmailNotFound(isVisible = viewState.hasSingInError)
    }

}

@Composable
private fun WelcomeContainer(
    modifier: Modifier = Modifier,
    onSingInClick: () -> Unit,
    onSigOutClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val lottieComposition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(R.raw.auth_animation)
        )

        val progress by animateLottieCompositionAsState(
            composition = lottieComposition,
            iterations = LottieConstants.IterateForever,
            isPlaying = lottieComposition != null // Запускаем анимацию только когда загружено
        )

        LottieAnimation(
            modifier = Modifier.size(400.dp),
            composition = lottieComposition,
            progress = progress
        )
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(com.example.locale.R.string.auth_welcome),
                textAlign = TextAlign.Center,
                style = TextStyles.Header20,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = stringResource(com.example.locale.R.string.auth_you_will_be),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp).weight(1f)
            )
        }

        AuthSignInButton(
            onClick = onSingInClick
        )
//        Text(
//            text = stringResource(com.example.locale.R.string.auth_or),
//            modifier = Modifier.padding(top = 16.dp)
//        )
//        Button(
//            onClick = {
//                onSigOutClick()
//            },
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color.Transparent,
//                contentColor = Color.Black
//            )
//        ) {
//            Text(text = stringResource(com.example.locale.R.string.auth_go_to_registration))
//        }
    }

}


@Composable
private fun AuthSignInButton(
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp)
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = ColorTheme.secondary, // Цвет фона
            contentColor = Color.White // Цвет текста и иконки
        ),
        onClick = {
            onClick()
        }
    ) {
        Row {

            Icon(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Google Icon",
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = stringResource(com.example.locale.R.string.auth_sing_in_with_goolge),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}


@Composable
private fun ShowEmailNotFound(isVisible: Boolean) {
    if (isVisible) {
        EmailNotFoundToast(message = stringResource(com.example.locale.R.string.auth_out_from_auth))
    }
}

@Composable
fun EmailNotFoundToast(message: String) {
    val context = LocalContext.current
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}