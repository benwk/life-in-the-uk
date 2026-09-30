package uk.robin.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Placeholder tokens from docs/design/design-brief.md; replace once the design is final.
object RobinColors {
    val Paper = Color(0xFFFAF6EE)
    val Ink = Color(0xFF1F2A44)
    val Robin = Color(0xFFE4572E)
    val RacingGreen = Color(0xFF1E4D3A)
    val Amber = Color(0xFFF2A541)
    val Fog = Color(0xFFC9CED6)
    val NightCarriage = Color(0xFF141A2A)

    private val lines = mapOf(
        "line-values" to Color(0xFF6C4AB6),
        "line-uk" to Color(0xFF2E86AB),
        "line-history" to Color(0xFFB5651D),
        "line-society" to Color(0xFF2A9D8F),
        "line-government" to Color(0xFF264653),
    )

    fun line(token: String): Color = lines[token] ?: Ink
}

private val LightColors = lightColorScheme(
    primary = RobinColors.Robin,
    onPrimary = Color.White,
    secondary = RobinColors.RacingGreen,
    background = RobinColors.Paper,
    onBackground = RobinColors.Ink,
    surface = Color.White,
    onSurface = RobinColors.Ink,
)

private val DarkColors = darkColorScheme(
    primary = RobinColors.Robin,
    secondary = Color(0xFF4F9C7C),
    background = RobinColors.NightCarriage,
    surface = Color(0xFF1D2438),
)

@Composable
fun RobinTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        content = content,
    )
}
