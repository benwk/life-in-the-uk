package uk.robin.app.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import uk.robin.app.data.LensMode
import uk.robin.core.LocalizedText
import uk.robin.core.translation

/**
 * English + learner-language text following the Language Lens.
 * Tapping flips between the two languages (docs/product/07-language-experience.md).
 */
@Composable
fun BilingualText(
    text: LocalizedText,
    locale: String,
    mode: LensMode,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
) {
    val english = text.getValue("en")
    val native = text.translation(locale)
    var flipped by remember(text, mode) { mutableStateOf(false) }

    Column(modifier.clickable(enabled = native != null) { flipped = !flipped }) {
        AnimatedContent(targetState = flipped, label = "flip") { isFlipped ->
            if (native == null) {
                Text(english, style = style)
            } else {
                val secondaryStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                )
                val nativeOnTop = (mode == LensMode.NATIVE_FIRST) != isFlipped
                when {
                    mode == LensMode.ENGLISH_FIRST && !isFlipped -> Text(english, style = style)
                    mode == LensMode.ENGLISH_FIRST -> Text(native, style = style)
                    else -> Column {
                        Text(if (nativeOnTop) native else english, style = style)
                        Spacer(Modifier.height(4.dp))
                        Text(if (nativeOnTop) english else native, style = secondaryStyle)
                    }
                }
            }
        }
    }
}
