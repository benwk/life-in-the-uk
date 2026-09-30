package uk.robin.app.ui.testday

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import uk.robin.app.AppState
import uk.robin.app.ui.components.QuestionCard
import uk.robin.core.ExamResult
import uk.robin.core.MockExam
import uk.robin.core.localized

/** Exam mode: English only, no mascot, calm timer (docs/product/06-mock-exam-journey.md). */
@Composable
fun MockExamScreen(state: AppState, mini: Boolean, onExit: () -> Unit) {
    val meta = state.bundle.meta
    val exam = remember(mini) {
        if (mini) MockExam.build(state.bundle, meta.miniMock.questionCount, meta.miniMock.durationMinutes)
        else MockExam.build(state.bundle)
    }
    var index by remember { mutableIntStateOf(0) }
    var secondsLeft by remember { mutableIntStateOf(exam.durationMinutes * 60) }
    var result by remember { mutableStateOf<ExamResult?>(null) }
    // Bumped on every answer/flag so Compose re-reads the (non-observable) MockExam.
    var revision by remember { mutableIntStateOf(0) }

    LaunchedEffect(exam) {
        while (secondsLeft > 0 && result == null) {
            delay(1000)
            secondsLeft--
        }
        if (result == null) result = exam.result()
    }

    val finished = result
    if (finished != null) {
        ResultView(state, exam, finished, onExit)
        return
    }
    if (exam.questions.isEmpty()) {
        Text("No exam questions yet.", Modifier.padding(16.dp))
        return
    }

    val question = exam.questions[index]
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Question ${index + 1} of ${exam.questions.size}")
            Text("%d:%02d".format(secondsLeft / 60, secondsLeft % 60))
        }
        key(revision) {
            QuestionCard(
                question = question,
                response = exam.response(question.id),
                onResponse = {
                    exam.answer(question.id, it)
                    revision++
                },
                locale = "en",
                examMode = true,
                revealed = false,
            )
            TextButton(onClick = {
                exam.toggleFlag(question.id)
                revision++
            }) { Text(if (exam.isFlagged(question.id)) "🚩 Flagged" else "Flag for review") }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = { index-- }, enabled = index > 0) { Text("Previous") }
            if (index < exam.questions.lastIndex) {
                Button(onClick = { index++ }) { Text("Next") }
            } else {
                Button(onClick = { result = exam.result() }) { Text("Submit") }
            }
        }
    }
}

@Composable
private fun ResultView(state: AppState, exam: MockExam, result: ExamResult, onExit: () -> Unit) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("${result.score} / ${result.total}", style = MaterialTheme.typography.displayMedium)
        Text(
            if (result.passed) "PASSED ✅" else "Needed ${result.requiredToPass} — keep going!",
            style = MaterialTheme.typography.titleLarge,
        )
        // Review: translations come back (docs/product/06-mock-exam-journey.md).
        exam.questions.filter { it.id in result.wrongQuestionIds }.forEach { q ->
            Text("• ${q.prompt.getValue("en")}", style = MaterialTheme.typography.bodyLarge)
            Text(q.explanation.localized(state.learnerLocale), style = MaterialTheme.typography.bodyMedium)
        }
        Button(onClick = onExit) { Text("Back to Test Day") }
    }
}
