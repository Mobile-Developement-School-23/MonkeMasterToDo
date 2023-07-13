package com.monke.yandextodo.presentation.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable


val lightColorPalette = lightColors(
    primary = LightBackPrimary,
    onPrimary = LightLabelPrimary,
    secondary = LightBackSecondary,
    background = LightBackPrimary,
    onBackground = LightLabelPrimary,
    surface = LightBackSecondary,
    secondaryVariant = Blue
)

val darkColorPalette = darkColors(
    primary = DarkBackPrimary,
    onPrimary = DarkLabelPrimary,
    secondary = DarkBackSecondary,
    background = DarkBackPrimary,
    onBackground = DarkLabelPrimary,
    surface = DarkBackSecondary,
    secondaryVariant = Blue
)


@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) darkColorPalette else lightColorPalette,
        content = content,
        typography = typography
    )

}


