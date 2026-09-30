package uk.robin.app.ui.practice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uk.robin.app.AppState
import uk.robin.app.ui.components.QuestionCard
import uk.robin.core.Grader
import uk.robin.core.QuestionType
import uk.robin.core.TeaBreak

/** Tea Break (no station) or practice for one station. */
@Composable
fun PracticeScreen(state: AppState, stationId: String?) {
    val questions = remember(stationId) {
        val pool = (stationId?.let(state.bundle::questions) ?: state.bundle.questions)
            .filter { it.type != QuestionType.MATCH }
        if (stationId == null) {
            TeaBreak.pick(pool, state.bundle.meta.dailyTeaBreak.questionCount, state.mistakes, state.seen)
        } else {
            pool.shuffled()
        }
    }
    var index by remember { mutableIntStateOf(0) }
    var response by remember { mutableStateOf(emptyList<String>()) }
    var revealed by remember { mutableStateOf(false) }
    var correctCount by remember { mutableIntStateOf(0) }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (questions.isEmpty()) {
            Text("No questions here yet.")
            return@Column
        }
        if (index == questions.size) {
            Text("☕ Tea finished!", style = MaterialTheme.typography.headlineMedium)
            Text("$correctCount / ${questions.size} correct")
            return@Column
        }

        val question = questions[index]
        LinearProgressIndicator(progress = { index / questions.size.toFloat() })
        QuestionCard(
            question = question,
            response = response,
            onResponse = { response = it },
            locale = state.learnerLocale,
            examMode = false,
            revealed = revealed,
        )
        if (!revealed) {
            Button(
                onClick = {
                    val correct = Grader.isCorrect(question, response)
                    if (correct) correctCount++
                    state.recordAnswer(question.id, correct)
                    revealed = true
                },
                enabled = response.size == question.answer.size,
            ) { Text("Check") }
        } else {
            Button(onClick = {
                index++
                response = emptyList()
                revealed = false
            }) { Text("Next") }
        }
    }
}
