package uk.robin.app.ui.journey

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import uk.robin.app.AppState
import uk.robin.app.ui.theme.RobinColors
import uk.robin.core.Station
import uk.robin.core.localized

/**
 * Placeholder for the railway line map (docs/product/04-learning-journey.md):
 * each line is a coloured rail with its stations. The real map is waiting on the design.
 */
@Composable
fun JourneyScreen(state: AppState, onStation: (String) -> Unit) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)) {
        state.bundle.lines.forEach { line ->
            val color = RobinColors.line(line.color)
            item(key = line.id) {
                Column(Modifier.padding(top = 16.dp, bottom = 8.dp)) {
                    Text(line.title.localized(state.learnerLocale), style = MaterialTheme.typography.titleLarge, color = color)
                    Text(line.title.getValue("en"), style = MaterialTheme.typography.bodySmall)
                }
            }
            items(line.stations, key = { it.id }) { station ->
                StationRow(
                    station = station,
                    color = color,
                    stamped = station.id in state.stamps,
                    locale = state.learnerLocale,
                    onClick = { onStation(station.id) },
                )
            }
        }
    }
}

@Composable
private fun StationRow(station: Station, color: Color, stamped: Boolean, locale: String, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(Modifier.width(24.dp), contentAlignment = Alignment.Center) {
            Box(Modifier.width(6.dp).height(56.dp).background(color))
            Box(
                Modifier
                    .size(20.dp)
                    .background(if (stamped) color else MaterialTheme.colorScheme.background, CircleShape)
                    .border(3.dp, color, CircleShape),
            )
        }
        Column(Modifier.weight(1f)) {
            Text(station.title.localized(locale), style = MaterialTheme.typography.bodyLarge)
            Text(
                "${station.title.getValue("en")} · ${station.minutes} min" + if (stamped) " · 🛂" else "",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
