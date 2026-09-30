package uk.robin.app.ui.testday

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uk.robin.app.AppState

@Composable
fun TestDayScreen(state: AppState, onStart: (mini: Boolean) -> Unit) {
    val meta = state.bundle.meta
    // Simplified readiness until FSRS mastery exists: share of stations stamped.
    val readiness = state.stamps.size / state.bundle.stations.size.toFloat()

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("🎫 Test Day", style = MaterialTheme.typography.headlineMedium)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Readiness ${(readiness * 100).toInt()}%", style = MaterialTheme.typography.titleLarge)
                LinearProgressIndicator(progress = { readiness }, modifier = Modifier.fillMaxWidth())
                Text("This is an estimate, not a guarantee.", style = MaterialTheme.typography.bodySmall)
            }
        }
        Button(onClick = { onStart(false) }, modifier = Modifier.fillMaxWidth()) {
            Text("Full mock · ${meta.exam.questionCount} questions · ${meta.exam.durationMinutes} min")
        }
        OutlinedButton(onClick = { onStart(true) }, modifier = Modifier.fillMaxWidth()) {
            Text("Mini mock · ${meta.miniMock.questionCount} questions · ${meta.miniMock.durationMinutes} min")
        }
        Text("Pass mark: ${meta.exam.passMark} / ${meta.exam.questionCount}", style = MaterialTheme.typography.bodySmall)
    }
}
