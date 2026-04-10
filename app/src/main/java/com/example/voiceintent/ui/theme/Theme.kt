package com.example.voiceintent.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Blue600,
    onPrimary = White,
    primaryContainer = Blue100,
    onPrimaryContainer = Blue800,

    // No Secondary / Tertiary — intentionally minimal
    secondary = Blue600,
    onSecondary = White,
    secondaryContainer = Blue100,
    onSecondaryContainer = Blue800,

    background = Grey50,
    onBackground = Grey900,

    surface = White,
    onSurface = Grey900,
    surfaceVariant = Grey50,
    onSurfaceVariant = Grey600,

    outline = Grey300,
    outlineVariant = Grey100,

    error = Red500,
    onError = White,
    errorContainer = Red50,
    onErrorContainer = Red500,
)

private val DarkColorScheme = darkColorScheme(
    primary = Blue400,
    onPrimary = Color(0xFF003060),
    primaryContainer = Blue800,
    onPrimaryContainer = Blue100,

    secondary = Blue400,
    onSecondary = Color(0xFF003060),
    secondaryContainer = Blue800,
    onSecondaryContainer = Blue100,

    background = Color(0xFF1A1C1E),
    onBackground = Color(0xFFE2E2E6),

    surface = Color(0xFF1A1C1E),
    onSurface = Color(0xFFE2E2E6),
    surfaceVariant = Color(0xFF43474E),
    onSurfaceVariant = Color(0xFFC3C7CF),

    outline = Color(0xFF8D9199),
    outlineVariant = Color(0xFF43474E),

    error = Color(0xFFFF897D),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
)

val ColorScheme.positive: Color
    get() = Green600

val ColorScheme.positiveContainer: Color
    get() = Green50

val ColorScheme.onPositiveContainer: Color
    get() = Color(0xFF3B6D11)

val ColorScheme.neutral: Color
    get() = GreyWarm

val ColorScheme.neutralContainer: Color
    get() = GreyWarm50

val ColorScheme.onNeutralContainer: Color
    get() = Color(0xFF5F5E5A)

val ColorScheme.negative: Color
    get() = error          // Color(0xFFE24B4A) в light, 0xFFFF897D в dark

val ColorScheme.negativeContainer: Color
    get() = errorContainer

val ColorScheme.waveformProgress: Color
    get() = Blue400

@Composable
fun VoiceIntentTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = VoiceIntentTypography,
        content = content
    )
}