package uk.robin.app.ui.journey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uk.robin.app.AppState
import uk.robin.app.ui.components.BilingualText
import uk.robin.core.Fact
import uk.robin.core.localized

/** A station: swipeable story cards, then practice and the passport stamp. */
@Composable
fun StationScreen(state: AppState, stationId: String, onPractice: (String) -> Unit) {
    val bundle = state.bundle
    val station = bundle.stations.firstOrNull { it.id == stationId } ?: return
    val facts = bundle.facts(stationId)
    val pager = rememberPagerState { facts.size + 1 }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(station.title.localized(state.learnerLocale), style = MaterialTheme.typography.headlineSmall)
        Text(station.title.getValue("en"), style = MaterialTheme.typography.bodySmall)

        HorizontalPager(pager, Modifier.weight(1f).fillMaxWidth(), pageSpacing = 12.dp) { page ->
            if (page < facts.size) {
                StoryCard(facts[page], state)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("🛂 End of the line", style = MaterialTheme.typography.titleLarge)
                    Button(onClick = { onPractice(stationId) }) { Text("Platform quiz") }
                    OutlinedButton(onClick = { state.collectStamp(stationId) }, enabled = stationId !in state.stamps) {
                        Text(if (stationId in state.stamps) "Stamp collected" else "Collect stamp")
                    }
                }
            }
        }
        if (facts.isEmpty()) {
            Text("Story cards for this station are still being written.", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun StoryCard(fact: Fact, state: AppState) {
    Card(Modifier.fillMaxSize()) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            fact.year?.let { Text("$it", style = MaterialTheme.typography.displayMedium) }
            BilingualText(fact.text, state.learnerLocale, state.lensMode)
            fact.memoryHook?.get(state.learnerLocale)?.let { Text("💡 $it", style = MaterialTheme.typography.bodyMedium) }
            fact.examNote?.let { Text("⚠️ ${it.localized(state.learnerLocale)}", style = MaterialTheme.typography.bodySmall) }
        }
    }
}
