package uk.robin.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/** Text keyed by locale code ("en", "zh-Hans", …). English is always present. */
typealias LocalizedText = Map<String, String>

/** Returns the text in [locale], falling back to English. */
fun LocalizedText.localized(locale: String): String = this[locale] ?: this.getValue("en")

/** Returns the text in [locale] only if it has been translated. */
fun LocalizedText.translation(locale: String): String? = if (locale == "en") null else this[locale]

/** The single bundle built by tools/build_content.py and shipped in the app. */
@Serializable
data class ContentBundle(
    val meta: Meta,
    val lines: List<Line>,
    val facts: List<Fact>,
    val questions: List<Question>,
    val terms: List<Term>,
) {
    val stations: List<Station> get() = lines.flatMap { it.stations }

    fun line(stationId: String): Line? = lines.firstOrNull { line -> line.stations.any { it.id == stationId } }
    fun facts(stationId: String): List<Fact> = facts.filter { it.stationId == stationId }
    fun questions(stationId: String): List<Question> = questions.filter { it.stationId == stationId }

    companion object {
        private val json = Json { ignoreUnknownKeys = true }

        fun decode(text: String): ContentBundle = json.decodeFromString(serializer(), text)
    }
}

@Serializable
data class Meta(
    val contentVersion: String,
    val handbook: Handbook,
    val locales: List<ContentLocale>,
    val exam: ExamRules,
    val miniMock: MockRules,
    val dailyTeaBreak: TeaBreakRules,
)

@Serializable
data class Handbook(val title: String, val edition: String)

@Serializable
data class ContentLocale(val code: String, val name: String, val dir: String) {
    val isRightToLeft: Boolean get() = dir == "rtl"
}

@Serializable
data class ExamRules(val questionCount: Int, val durationMinutes: Int, val passMark: Int)

@Serializable
data class MockRules(val questionCount: Int, val durationMinutes: Int)

@Serializable
data class TeaBreakRules(val questionCount: Int)

@Serializable
data class Line(
    val id: String,
    val chapter: Int,
    val color: String,
    val title: LocalizedText,
    val stations: List<Station>,
)

@Serializable
data class Station(
    val id: String,
    val title: LocalizedText,
    val summary: LocalizedText? = null,
    val minutes: Int,
    val stampIcon: String,
)

@Serializable
data class Fact(
    val id: String,
    val stationId: String,
    val kind: String,
    val year: Int? = null,
    val text: LocalizedText,
    val memoryHook: LocalizedText? = null,
    val examNote: LocalizedText? = null,
    val termIds: List<String> = emptyList(),
    val source: Source,
)

@Serializable
data class Source(val chapter: Int, val section: String)

@Serializable
enum class QuestionType {
    @SerialName("single") SINGLE,
    @SerialName("multiple") MULTIPLE,
    @SerialName("true_false") TRUE_FALSE,
    @SerialName("statement") STATEMENT,
    @SerialName("order") ORDER,
    @SerialName("match") MATCH;

    /** Types that appear in the real test and therefore in mock exams. */
    val isExamType: Boolean get() = this != ORDER && this != MATCH
}

@Serializable
data class Question(
    val id: String,
    val stationId: String,
    val factIds: List<String>,
    val type: QuestionType,
    val prompt: LocalizedText,
    val options: List<Option>,
    val answer: List<String>,
    val explanation: LocalizedText,
    val difficulty: Int,
    val examEligible: Boolean,
    val traps: List<String> = emptyList(),
)

@Serializable
data class Option(
    val id: String,
    val text: LocalizedText,
    val matchText: LocalizedText? = null,
)

@Serializable
data class Term(
    val id: String,
    val term: String,
    val ipa: String? = null,
    val definition: LocalizedText,
)
