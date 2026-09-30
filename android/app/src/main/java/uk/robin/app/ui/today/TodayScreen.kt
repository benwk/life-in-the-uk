package uk.robin.app.ui.today

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uk.robin.app.AppState
import uk.robin.core.localized

@Composable
fun TodayScreen(
    state: AppState,
    onTeaBreak: () -> Unit,
    onStation: (String) -> Unit,
    onTestDay: () -> Unit,
) {
    val bundle = state.bundle
    val next = bundle.stations.firstOrNull { it.id !in state.stamps }
    val teaBreakSize = bundle.meta.dailyTeaBreak.questionCount

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("🐦 Hello!", style = MaterialTheme.typography.headlineMedium)

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("☕ Today's Tea Break", style = MaterialTheme.typography.titleLarge)
                Text("$teaBreakSize questions · about 3 minutes")
                Button(onClick = onTeaBreak) { Text("Start") }
            }
        }

        if (next != null) {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("🚉 Next stop", style = MaterialTheme.typography.titleLarge)
                    Text(next.title.localized(state.learnerLocale))
                    Text(next.title.getValue("en"), style = MaterialTheme.typography.bodySmall)
                    OutlinedButton(onClick = { onStation(next.id) }) { Text("Continue the journey") }
                }
            }
        }

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("🎫 Test Day", style = MaterialTheme.typography.titleLarge)
                Text("Stamps: ${state.stamps.size} / ${bundle.stations.size}")
                OutlinedButton(onClick = onTestDay) { Text("See your readiness") }
            }
        }
    }
}
