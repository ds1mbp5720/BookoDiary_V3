package lee.project.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


private val LightColorPalette = BookDiaryColors(
    brand = Brown2,
    brandSecondary = Brown0,
    uiBackground = Neutral0,
    uiBorder = Neutral4,
    uiFloated = FunctionalGrey,
    textPrimary = Neutral7,
    textSecondary = Neutral1,
    textHelp = Neutral6,
    textInteractive = Neutral2,
    textLink = Brown9,
    iconSecondary = Neutral7,
    iconInteractive = Neutral0,
    iconInteractiveInactive = Neutral1,
    error = FunctionalRed,
    gradient6_1 = listOf(Brown4, Brown1, Brown2, Brown1, Brown4),
    gradient6_2 = listOf(Ocean8, Ocean3, Ocean2, Ocean1, Ocean4),
    gradient3_1 = listOf(Brown2, Brown1, Brown4),
    gradient2_1 = listOf(Brown1, Brown0),
    gradient2_2 = listOf(Red1, Red3),
    tornado1 = listOf(Brown3, Brown0),
    isDark = false
)

private val DarkColorPalette = BookDiaryColors(
    brand = Brown2,
    brandSecondary = Brown1,
    uiBackground = Neutral0,
    uiBorder = Neutral4,
    uiFloated = FunctionalDarkGrey,
    textPrimary = Neutral7,
    textSecondary = Neutral7,
    textHelp = Neutral6,
    textInteractive = Neutral0,
    textLink = Brown9,
    iconSecondary = Neutral7,
    iconInteractive = Neutral0,
    iconInteractiveInactive = Neutral1,
    error = FunctionalRedDark,
    gradient6_1 = listOf(Brown4, Brown1, Brown2, Brown1, Brown4),
    gradient6_2 = listOf(Brown4, Brown1, Brown2, Brown1, Brown4),
    gradient3_1 = listOf(Brown2, Brown1, Brown4),
    gradient2_1 = listOf(Brown2, Brown0),
    gradient2_2 = listOf(Brown1, Brown3),
    tornado1 = listOf(Brown1, Brown0),
    isDark = true
)


private val DarkColorScheme = darkColorScheme(
    primary = Brown0,
    secondary = Brown2,
    tertiary = Brown1
)

private val LightColorScheme = lightColorScheme(
    primary = Brown0,
    secondary = Brown2,
    tertiary = Brown1
)

@Composable
fun BookDiaryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    provideBookDiaryColors(colors = colors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

object BookDiaryTheme {
    val colors: BookDiaryColors
    @Composable
    get() = LocalBookDiaryColors.current
}
@Stable
class BookDiaryColors(
    gradient6_1: List<Color>,
    gradient6_2: List<Color>,
    gradient3_1: List<Color>,
    gradient2_1: List<Color>,
    gradient2_2: List<Color>,
    brand: Color,
    brandSecondary: Color,
    uiBackground: Color,
    uiBorder: Color,
    uiFloated: Color,
    interactivePrimary: List<Color> = gradient2_1,
    interactiveSecondary: List<Color> = gradient2_2,
    interactiveMask: List<Color> = gradient6_1,
    textPrimary: Color = brand,
    textSecondary: Color,
    textHelp: Color,
    textInteractive: Color,
    textLink: Color,
    tornado1: List<Color>,
    iconPrimary: Color = brand,
    iconSecondary: Color,
    iconInteractive: Color,
    iconInteractiveInactive: Color,
    error: Color,
    notificationBadge: Color = error,
    isDark: Boolean
) {
    var gradient6_1 by mutableStateOf(gradient6_1)
        private set
    var gradient6_2 by mutableStateOf(gradient6_2)
        private set
    var gradient3_1 by mutableStateOf(gradient3_1)
        private set
    var gradient2_1 by mutableStateOf(gradient2_1)
        private set
    var gradient2_2 by mutableStateOf(gradient2_2)
        private set
    var brand by mutableStateOf(brand)
        private set
    var brandSecondary by mutableStateOf(brandSecondary)
        private set
    var uiBackground by mutableStateOf(uiBackground)
        private set
    var uiBorder by mutableStateOf(uiBorder)
        private set
    var uiFloated by mutableStateOf(uiFloated)
        private set
    var interactivePrimary by mutableStateOf(interactivePrimary)
        private set
    var interactiveSecondary by mutableStateOf(interactiveSecondary)
        private set
    var interactiveMask by mutableStateOf(interactiveMask)
        private set
    var textPrimary by mutableStateOf(textPrimary)
        private set
    var textSecondary by mutableStateOf(textSecondary)
        private set
    var textHelp by mutableStateOf(textHelp)
        private set
    var textInteractive by mutableStateOf(textInteractive)
        private set
    var tornado1 by mutableStateOf(tornado1)
        private set
    var textLink by mutableStateOf(textLink)
        private set
    var iconPrimary by mutableStateOf(iconPrimary)
        private set
    var iconSecondary by mutableStateOf(iconSecondary)
        private set
    var iconInteractive by mutableStateOf(iconInteractive)
        private set
    var iconInteractiveInactive by mutableStateOf(iconInteractiveInactive)
        private set
    var error by mutableStateOf(error)
        private set
    var notificationBadge by mutableStateOf(notificationBadge)
        private set
    var isDark by mutableStateOf(isDark)
        private set

    fun update(other: BookDiaryColors) {
        gradient6_1 = other.gradient6_1
        gradient6_2 = other.gradient6_2
        gradient3_1 = other.gradient3_1
        gradient2_1 = other.gradient2_1
        gradient2_2 = other.gradient2_2
        brand = other.brand
        brandSecondary = other.brandSecondary
        uiBackground = other.uiBackground
        uiBorder = other.uiBorder
        uiFloated = other.uiFloated
        interactivePrimary = other.interactivePrimary
        interactiveSecondary = other.interactiveSecondary
        interactiveMask = other.interactiveMask
        textPrimary = other.textPrimary
        textSecondary = other.textSecondary
        textHelp = other.textHelp
        textInteractive = other.textInteractive
        textLink = other.textLink
        tornado1 = other.tornado1
        iconPrimary = other.iconPrimary
        iconSecondary = other.iconSecondary
        iconInteractive = other.iconInteractive
        iconInteractiveInactive = other.iconInteractiveInactive
        error = other.error
        notificationBadge = other.notificationBadge
        isDark = other.isDark
    }

    fun copy(): BookDiaryColors = BookDiaryColors(
        gradient6_1 = gradient6_1,
        gradient6_2 = gradient6_2,
        gradient3_1 = gradient3_1,
        gradient2_1 = gradient2_1,
        gradient2_2 = gradient2_2,
        brand = brand,
        brandSecondary = brandSecondary,
        uiBackground = uiBackground,
        uiBorder = uiBorder,
        uiFloated = uiFloated,
        interactivePrimary = interactivePrimary,
        interactiveSecondary = interactiveSecondary,
        interactiveMask = interactiveMask,
        textPrimary = textPrimary,
        textSecondary = textSecondary,
        textHelp = textHelp,
        textInteractive = textInteractive,
        textLink = textLink,
        tornado1 = tornado1,
        iconPrimary = iconPrimary,
        iconSecondary = iconSecondary,
        iconInteractive = iconInteractive,
        iconInteractiveInactive = iconInteractiveInactive,
        error = error,
        notificationBadge = notificationBadge,
        isDark = isDark,
    )
}

@Composable
fun provideBookDiaryColors(
    colors: BookDiaryColors,
    content: @Composable () -> Unit
) {
    val colorPalette = remember {
        colors.copy()
    }
    colorPalette.update(colors)
    CompositionLocalProvider ( LocalBookDiaryColors provides colorPalette, content = content)
}

private val LocalBookDiaryColors = staticCompositionLocalOf<BookDiaryColors> {
    error("No BookDiaryColorPalette provided")
}
fun debugColors(
    debugColor: Color = Brown3
): ColorScheme = ColorScheme(
        primary = debugColor,
        onPrimary = debugColor,
        primaryContainer = debugColor,
        onPrimaryContainer = debugColor,
        inversePrimary = debugColor,
        secondary = debugColor,
        onSecondary = debugColor,
        secondaryContainer = debugColor,
        onSecondaryContainer = debugColor,
        tertiary = debugColor,
        onTertiary = debugColor,
        tertiaryContainer = debugColor,
        onTertiaryContainer = debugColor,
        background = debugColor,
        onBackground = debugColor,
        surface = debugColor,
        onSurface = debugColor,
        surfaceVariant = debugColor,
        onSurfaceVariant = debugColor,
        surfaceTint = debugColor,
        inverseSurface = debugColor,
        inverseOnSurface = debugColor,
        error = debugColor,
        onError = debugColor,
        errorContainer = debugColor,
        onErrorContainer = debugColor,
        outline = debugColor,
        outlineVariant = debugColor,
        scrim = debugColor
)
