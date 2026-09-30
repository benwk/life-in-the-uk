package uk.robin.app.ui.passport

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uk.robin.app.AppState
import uk.robin.app.data.LensMode
import uk.robin.core.localized

@Composable
fun PassportScreen(state: AppState) {
    val bundle = state.bundle
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("🛂 Passport", style = MaterialTheme.typography.headlineMedium)
        Text("${state.stamps.size} / ${bundle.stations.size} stamps")
        bundle.stations.filter { it.id in state.stamps }.forEach {
            Text("🔖 ${it.title.localized(state.learnerLocale)}")
        }

        Text("Learning language", style = MaterialTheme.typography.titleMedium)
        bundle.meta.locales.filter { it.code != "en" }.forEach { locale ->
            FilterChip(
                selected = state.learnerLocale == locale.code,
                onClick = { state.updateLearnerLocale(locale.code) },
                label = { Text(locale.name) },
            )
        }

        Text("Language Lens", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            LensMode.entries.forEach { mode ->
                FilterChip(
                    selected = state.lensMode == mode,
                    onClick = { state.updateLensMode(mode) },
                    label = {
                        Text(
                            when (mode) {
                                LensMode.NATIVE_FIRST -> "Native first"
                                LensMode.SIDE_BY_SIDE -> "Side by side"
                                LensMode.ENGLISH_FIRST -> "English first"
                            },
                        )
                    },
                )
            }
        }
    }
}
