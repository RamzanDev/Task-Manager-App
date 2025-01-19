package com.example.uikit.theme

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

object ColorTheme {

    var isInDarkTheme: MutableState<Boolean> = mutableStateOf(false)

    private fun getColor(lightColor: Color, darkColor: Color): Color {
        return if (isInDarkTheme.value) darkColor else lightColor
    }

    val brandPrimary: Color
        get() = getColor(brandPrimaryLight, brandPrimaryDark)

    val primary: Color
        get() = getColor(primaryLight, primaryDark)

    val secondary: Color
        get() = getColor(secondaryLight, secondaryDark)

    val tertiary: Color
        get() = getColor(tertiaryLight, tertiaryDark)

    val quaternary: Color
        get() = getColor(quaternaryLight, quaternaryDark)

    val inverse: Color
        get() = getColor(inverseLight, inverseDark)

    val staticPrimary: Color
        get() = getColor(staticPrimaryLight, staticPrimaryDark)

    val staticInverse: Color
        get() = getColor(staticInverseLight, staticInverseDark)

    val quinary: Color
        get() = getColor(quinaryLight, quinaryDark)

    val bg: Color
        get() = getColor(bgLight, bgDark)

    val errorSurface: Color
        get() = getColor(errorSurfaceLight, errorSurfaceDark)

    val warningSurface: Color
        get() = getColor(warningSurfaceLight, warningSurfaceDark)

    val successSurface: Color
        get() = getColor(successSurfaceLight, successSurfaceDark)

    val errorElement: Color
        get() = getColor(errorElementsLight, errorElementsDark)

    val warningElements: Color
        get() = getColor(warningElementsLight, warningElementsDark)

    val successText: Color
        get() = getColor(successTextLight, successTextDark)

}