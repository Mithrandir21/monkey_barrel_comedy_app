package pm.bam.mbc.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf


private val lightScheme = Colors(
    primary = primaryLight,
    primaryVariant = primaryVariantLight,
    onPrimary = onPrimaryLight,
    secondary = secondaryLight,
    secondaryVariant = secondaryVariantLight,
    onSecondary = onSecondaryLight,
    error = errorLight,
    onError = onErrorLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    isLight = true
)

private val darkScheme = Colors(
    primary = primaryDark,
    primaryVariant = primaryVariantDark,
    onPrimary = onPrimaryDark,
    secondary = secondaryDark,
    secondaryVariant = secondaryVariantDark,
    onSecondary = onSecondaryDark,
    error = errorDark,
    onError = onErrorDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    isLight = false
)



val LocalExtendedSpacing = staticCompositionLocalOf { CustomSpaces() }


@Composable
fun MonkeyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkScheme
        else -> lightScheme
    }

    val extendedSpacing = CustomSpaces()

    CompositionLocalProvider(LocalExtendedSpacing provides extendedSpacing) {
        MaterialTheme(
            colors = colorScheme,
            content = content
        )
    }
}



/**
 * Provides access to the custom spacing values defined in the theme.
 */
object MonkeyCustomTheme {
    val spacing: CustomSpaces
        @Composable
        get() = LocalExtendedSpacing.current
}