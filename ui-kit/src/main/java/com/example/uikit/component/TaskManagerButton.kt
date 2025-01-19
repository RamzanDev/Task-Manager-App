package com.example.uikit.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.uikit.theme.ColorTheme
import com.example.uikit.theme.CornerStyles
import com.example.uikit.theme.TaskManagerMargin
import com.example.uikit.theme.TextStyles

@Preview
@Composable
fun TaskManagerButton(
    modifier: Modifier = Modifier,
    style: ButtonStyles = ButtonStyles.PrimaryMd,
    state: ButtonState = ButtonState.Enabled(false),
    icon: @Composable (Modifier) -> Unit = {},
    text: String = "Hello",
    onClick: () -> Unit = {},
) {
    ElevatedButton(
        modifier =
        modifier
            .fillMaxWidth()
            .height(style.height),
        shape = RoundedCornerShape(style.corners),
        colors =
        ButtonColors(
            contentColor = style.textColor,
            containerColor = style.bgColor,
            disabledContentColor = ColorTheme.primary,
            disabledContainerColor = ColorTheme.quinary,
        ),
        enabled = state.clickable,
        onClick = { onClick() },
    ) {

        when (state) {
            ButtonState.Loading -> {
                IndeterminateCircularIndicator(
                    modifier = Modifier.align(Alignment.CenterVertically),
                )
            }

            else -> {
                icon(Modifier.align(Alignment.CenterVertically))
                Spacer(
                    modifier =
                    Modifier
                        .size(TaskManagerMargin.S)
                        .align(Alignment.CenterVertically),
                )
                Text(
                    text = text,
                    style = TextStyles.Body16Semibold,
                    color = if (state.clickable) {
                        style.textColor
                    } else {
                        ColorTheme.primary
                    },
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }
        }
    }
}

enum class ButtonStyles(
    val bgColor: Color = ColorTheme.brandPrimary,
    val height: Dp = 48.dp,
    val contentColor: Color = ColorTheme.staticInverse,
    val textColor: Color = ColorTheme.staticInverse,
    val corners: Dp = CornerStyles.L,
) {
    PrimaryMd(
        bgColor = ColorTheme.brandPrimary,
        contentColor = ColorTheme.staticInverse,
        height = 48.dp,
        textColor = ColorTheme.staticInverse,
        corners = CornerStyles.L,
    ),
    PrimaryLg(
        bgColor = ColorTheme.brandPrimary,
        contentColor = ColorTheme.staticInverse,
        height = 56.dp,
        textColor = ColorTheme.staticInverse,
        corners = CornerStyles.L,
    ),
}

sealed class ButtonState(
    val clickable: Boolean,
) {
    data class Enabled(
        val isEnabled: Boolean,
    ) : ButtonState(
        clickable = isEnabled,
    )

    data object Loading : ButtonState(clickable = false)
}
