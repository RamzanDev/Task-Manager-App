package com.example.uikit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.uikit.theme.ColorTheme
import com.example.uikit.theme.CornerStyles
import com.example.uikit.theme.PaddingStyles
import com.example.uikit.theme.TextStyles

@Composable
fun OtpView(
    modifier: Modifier,
    onOtpChanged: (String) -> Unit
) {

    var inputIsForced by remember { mutableStateOf(false) }
    var focusRequester = remember { FocusRequester() }
    var otpText by remember {
        mutableStateOf("")
    }

    BasicTextField(
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged {
                inputIsForced = it.isFocused
            },
        value = otpText,
        onValueChange = {
            if (it.length <= 4) {
                otpText = it
                onOtpChanged.invoke(it)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(PaddingStyles.L)
        ) {
            repeat(4) { index ->
                val number = when {
                    index >= otpText.length -> ""
                    else -> otpText[index]
                }

                Box(
                    modifier = Modifier
                        .size(width = 58.dp, height = 64.dp)
                        .clip(RoundedCornerShape(CornerStyles.XXL))
                        .background(ColorTheme.quaternary)
                ) {
                    Text(
                        text = number.toString(),
                        style = TextStyles.Header20,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}