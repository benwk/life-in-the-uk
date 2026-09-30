package uk.robin.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import uk.robin.app.ui.theme.RobinColors
import uk.robin.core.Question
import uk.robin.core.QuestionType
import uk.robin.core.TrapRadar
import uk.robin.core.localized
import uk.robin.core.translation

/**
 * Renders a question and collects a response. Used by practice (with
 * translations, trap radar and feedback) and by exam mode (English only).
 *
 * @param revealed when true, shows correct/incorrect colouring.
 */
@Composable
fun QuestionCard(
    question: Question,
    response: List<String>,
    onResponse: (List<String>) -> Unit,
    locale: String,
    examMode: Boolean,
    revealed: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        val english = question.prompt.getValue("en")
        Text(
            if (examMode) AnnotatedString(english) else highlightTraps(english),
            style = MaterialTheme.typography.titleLarge,
        )
        if (!examMode) {
            question.prompt.translation(locale)?.let {
                Text(it, style = MaterialTheme.typography.bodyMedium)
            }
        }
        when (question.type) {
            QuestionType.MULTIPLE -> Text("Choose TWO answers", style = MaterialTheme.typography.labelLarge)
            QuestionType.ORDER -> Text("Tap in order, earliest first", style = MaterialTheme.typography.labelLarge)
            else -> Unit
        }

        if (question.type == QuestionType.MATCH) {
            // TODO: drag-to-match mini game (docs/product/05-practice-journey.md).
            Text("Match Up is coming soon.")
            return@Column
        }

        question.options.forEach { option ->
            val index = response.indexOf(option.id)
            val selected = index >= 0
            val correct = option.id in question.answer
            val border = when {
                revealed && correct -> RobinColors.RacingGreen
                revealed && selected -> RobinColors.Amber
                selected -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.outline
            }
            OutlinedCard(
                onClick = { if (!revealed) onResponse(toggle(question, response, option.id)) },
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(if (selected || (revealed && correct)) 3.dp else 1.dp, border),
            ) {
                Column(Modifier.padding(16.dp)) {
                    val prefix = if (question.type == QuestionType.ORDER && selected) "${index + 1}. " else ""
                    Text(prefix + option.text.getValue("en"), style = MaterialTheme.typography.bodyLarge)
                    if (!examMode) {
                        option.text.translation(locale)?.let {
                            Text(it, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                    }
                }
            }
        }

        if (question.type == QuestionType.ORDER && response.isNotEmpty() && !revealed) {
            TextButton(onClick = { onResponse(emptyList()) }) { Text("Start again") }
        }

        if (revealed) {
            Text(question.explanation.localized(if (examMode) "en" else locale), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

private fun toggle(question: Question, response: List<String>, optionId: String): List<String> = when (question.type) {
    QuestionType.MULTIPLE ->
        if (optionId in response) response - optionId
        else (response + optionId).takeLast(question.answer.size)
    QuestionType.ORDER -> if (optionId in response) response else response + optionId
    else -> listOf(optionId)
}

private fun highlightTraps(text: String): AnnotatedString = buildAnnotatedString {
    append(text)
    TrapRadar.highlights(text).forEach {
        addStyle(SpanStyle(background = RobinColors.Amber.copy(alpha = 0.35f), fontWeight = FontWeight.Bold), it.start, it.end)
    }
}
