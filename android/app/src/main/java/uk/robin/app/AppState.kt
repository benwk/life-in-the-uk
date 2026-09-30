package uk.robin.app

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import uk.robin.app.data.LearnerStore
import uk.robin.app.data.LensMode
import uk.robin.core.ContentBundle

/** App-wide state: the content bundle plus the learner's settings and progress. */
class AppState(application: Application) : AndroidViewModel(application) {
    private val store = LearnerStore(application)

    val bundle: ContentBundle = application.assets.open("content.json").bufferedReader().use {
        ContentBundle.decode(it.readText())
    }

    /** Language used for translations, explanations and memory hooks. */
    var learnerLocale by mutableStateOf(store.learnerLocale)
        private set

    var lensMode by mutableStateOf(store.lensMode)
        private set

    var stamps by mutableStateOf(store.stamps)
        private set

    var mistakes by mutableStateOf(store.mistakes)
        private set

    var seen by mutableStateOf(store.seen)
        private set

    fun updateLearnerLocale(code: String) {
        learnerLocale = code
        store.learnerLocale = code
    }

    fun updateLensMode(mode: LensMode) {
        lensMode = mode
        store.lensMode = mode
    }

    fun collectStamp(stationId: String) {
        stamps = stamps + stationId
        store.stamps = stamps
    }

    fun recordAnswer(questionId: String, correct: Boolean) {
        seen = seen + questionId
        mistakes = if (correct) mistakes - questionId else mistakes + questionId
        store.seen = seen
        store.mistakes = mistakes
    }
}
