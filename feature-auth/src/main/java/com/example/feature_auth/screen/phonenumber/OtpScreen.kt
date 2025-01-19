package com.example.feature_auth.screen.phonenumber

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.navigation.BaseNavigator
import com.example.core.navigation.MockNavigator
import com.example.feature_auth.contract.OtpContract
import com.example.feature_auth.viewmodel.OtpViewModel
import com.example.uikit.component.ButtonState
import com.example.uikit.component.ButtonStyles
import com.example.uikit.component.OtpView
import com.example.uikit.component.TaskManagerButton
import com.example.uikit.theme.ColorTheme
import com.example.uikit.theme.CornerStyles
import com.example.uikit.theme.PaddingStyles
import com.example.uikit.theme.TextStyles

@Composable
fun OTPInputField(
    navigator: BaseNavigator = MockNavigator,
    viewModel: OtpViewModel = hiltViewModel()
) {

    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    Scaffold { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorTheme.inverse)
                .padding(paddingValues)
        ) {
            val (container, icons) = createRefs()
            OtpContainer(
                modifier =
                Modifier
                    .wrapContentSize()
                    .constrainAs(container) {
                        bottom.linkTo(icons.top)
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                viewState = viewState,
                onOtpEntered = {},
                onButtonClicked = {}
            )
        }
    }

}

@Composable
fun OtpContainer(
    modifier: Modifier = Modifier,
    viewState: OtpContract.ViewState,
    onOtpEntered: (String) -> Unit = {},
    onButtonClicked: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .wrapContentHeight()
            .clip(RoundedCornerShape(CornerStyles.XXL))
            .background(ColorTheme.inverse)
            .padding(vertical = PaddingStyles.XXL, horizontal = PaddingStyles.L)
    ) {

        Text(
            text = stringResource(id = com.example.locale.R.string.auth_enter_otp_code),
            style = TextStyles.Header20,
            color = ColorTheme.primary
        )

        Spacer(modifier = Modifier.size(PaddingStyles.XL))

        OtpView(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onOtpChanged = onOtpEntered,
        )

        Spacer(modifier = Modifier.size(PaddingStyles.XXXL))

        val leftButtonText =
            if (viewState.isErrorVisible) {
                stringResource(id = com.example.locale.R.string.auth_wrong_otp_resend_code)
            } else stringResource(id = com.example.locale.R.string.auth_resend_otp_code)

        val timerText =
            if (viewState.timeLeft > 0) {
                String.format("(0:%02d)", viewState.timeLeft)
            } else {
                ""
            }
        val buttonText = leftButtonText + timerText

        TaskManagerButton(
            style = ButtonStyles.PrimaryMd,
            state = viewState.toButtonState(),
            text = buttonText,
            onClick = { onButtonClicked.invoke() }
        )
    }

}

private fun OtpContract.ViewState.toButtonState(): ButtonState =
    if (isLoading) {
        ButtonState.Loading
    } else {
        ButtonState.Enabled(timeLeft == 0)
    }