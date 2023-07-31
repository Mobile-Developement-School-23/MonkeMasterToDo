package com.monke.yandextodo.presentation.theme


import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.lang.reflect.WildcardType


val lightColorPalette = lightColors(
    primary = LightBackPrimary,
    onPrimary = LightLabelPrimary,
    secondary = LightBackSecondary,
    background = LightBackPrimary,
    onBackground = LightLabelPrimary,
    secondaryVariant = Blue,
    onError = LightLabelDisable,
    onSecondary = LightLabelSecondary,
    surface = LightBackElevated,
    onSurface = LightLabelSecondary
)

val darkColorPalette = darkColors(
    primary = DarkBackPrimary,
    onPrimary = DarkLabelPrimary,
    secondary = DarkBackSecondary,
    background = DarkBackPrimary,
    onBackground = DarkLabelPrimary,
    secondaryVariant = Blue,
    onError = DarkLabelDisable,
    onSecondary = DarkLabelSecondary,
    surface = DarkBackElevated,
    onSurface = DarkLabelTertiary
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

// Preview типографии
@Composable()
@Preview(showBackground = true)
fun TypographyPreview() {
    return Column {
        Text(
            "H1 - 34 sp",
            style = typography.h1
        )
        Text(
            "H2 - 22 sp",
            style = typography.h2
        )
        Text(
            "Body1 - 18 sp",
            style = typography.body1
        )
        Text(
            "Subtitle1 - 16 sp",
            style = typography.subtitle1
        )
        Text(
            "Button - 14 sp",
            style = typography.button
        )
        Text(
            "Body2 - 14 sp",
            style = typography.body2
        )
    }
}

// Preview палитры

@Composable
@Preview()
fun ColorsLightPreview() {
    Column {
        Row {
            ColorItem(LightSupportSeparator, White, ::LightSupportSeparator.name)
            ColorItem(LightSupportOverlay, Color.Black, ::LightSupportOverlay.name)
        }
        Row {
            ColorItem(Red, White, ::Red.name)
            ColorItem(Green, White, ::Green.name)
        }
        Row {
            ColorItem(Blue, White, ::Blue.name)
            ColorItem(Gray, White, ::Gray.name)
        }
        Row {
            ColorItem(GrayLight, Color.Black, ::GrayLight.name)
            ColorItem(White, Color.Black, ::White.name)
        }
        Row {
            ColorItem(LightLabelPrimary, White, ::LightLabelPrimary.name)
            ColorItem(LightLabelSecondary, White, ::LightLabelSecondary.name)
        }
        Row {
            ColorItem(LightLabelTertiary, White, ::LightLabelTertiary.name)
            ColorItem(LightLabelDisable, Color.Black, ::LightLabelDisable.name)
        }
        Row {
            ColorItem(LightBackPrimary, Color.Black, ::LightBackPrimary.name)
            ColorItem(LightBackSecondary, Color.Black, ::LightBackSecondary.name)
        }
        Row {
            ColorItem(LightBackElevated, Color.Black, ::LightBackElevated.name)
        }
    }
}


@Composable
@Preview()
fun ColorsDarkPreview() {
    Column {
        Row {
            ColorItem(DarkSupportSeparator, Color.Black, ::DarkSupportSeparator.name)
            ColorItem(DarkSupportOverlay, White, ::DarkSupportOverlay.name)
        }
        Row {
            ColorItem(Red, White, ::Red.name)
            ColorItem(Green, White, ::Green.name)
        }
        Row {
            ColorItem(Blue, White, ::Blue.name)
            ColorItem(Gray, White, ::Gray.name)
        }
        Row {
            ColorItem(DarkGrayLight, White, ::DarkGrayLight.name)
            ColorItem(White, Color.Black, ::White.name)
        }
        Row {
            ColorItem(DarkLabelPrimary, Color.Black, ::DarkLabelPrimary.name)
            ColorItem(DarkLabelSecondary, Color.Black, ::DarkLabelSecondary.name)
        }
        Row {
            ColorItem(DarkLabelTertiary, Color.Black, ::DarkLabelTertiary.name)
            ColorItem(DarkLabelDisable, Color.Black, ::DarkLabelDisable.name)
        }
        Row {
            ColorItem(DarkBackPrimary, White, ::DarkBackPrimary.name)
            ColorItem(DarkBackSecondary, White, ::DarkBackSecondary.name)
        }
        Row {
            ColorItem(DarkBackElevated, White, ::DarkBackElevated.name)
        }
    }
}


@Composable
fun ColorItem(
    color: Color,
    textColor: Color,
    colorName: String
) {
    return Column(
        modifier = Modifier
            .height(100.dp)
            .width(150.dp)
            .background(color),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            colorName,
            color = textColor
        )
        Text(
            "#" + color.value.toString(16).substring(0, 8).uppercase(),
            color = textColor
        )
    }
}


