package eu.heha.samayouwa.model

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

enum class ColorThemeToken(val fromTheme: (ColorScheme) -> Color) {
    primary(ColorScheme::primary),
    onPrimary(ColorScheme::onPrimary),
    primaryContainer(ColorScheme::primaryContainer),
    onPrimaryContainer(ColorScheme::onPrimaryContainer),
    inversePrimary(ColorScheme::inversePrimary),
    secondary(ColorScheme::secondary),
    onSecondary(ColorScheme::onSecondary),
    secondaryContainer(ColorScheme::secondaryContainer),
    onSecondaryContainer(ColorScheme::onSecondaryContainer),
    tertiary(ColorScheme::tertiary),
    onTertiary(ColorScheme::onTertiary),
    tertiaryContainer(ColorScheme::tertiaryContainer),
    onTertiaryContainer(ColorScheme::onTertiaryContainer),
    background(ColorScheme::background),
    onBackground(ColorScheme::onBackground),
    surface(ColorScheme::surface),
    onSurface(ColorScheme::onSurface),
    surfaceVariant(ColorScheme::surfaceVariant),
    onSurfaceVariant(ColorScheme::onSurfaceVariant),
    surfaceTint(ColorScheme::surfaceTint),
    inverseSurface(ColorScheme::inverseSurface),
    inverseOnSurface(ColorScheme::inverseOnSurface),
    error(ColorScheme::error),
    onError(ColorScheme::onError),
    errorContainer(ColorScheme::errorContainer),
    onErrorContainer(ColorScheme::onErrorContainer),
    outline(ColorScheme::outline),
    outlineVariant(ColorScheme::outlineVariant)
}