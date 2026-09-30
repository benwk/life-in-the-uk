package uk.robin.core

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

/** Runs content/fixtures/core-cases.json, the cases shared with ios/RobinCore. */
class CoreCasesTest {
    @Serializable
    data class GradingCase(val name: String, val type: QuestionType, val answer: List<String>, val response: List<String>, val correct: Boolean)

    @Serializable
    data class PassMarkCase(val name: String, val questionCount: Int, val passMark: Int, val total: Int, val score: Int, val passed: Boolean)

    @Serializable
    data class TrapCase(val text: String, val traps: List<String>)

    @Serializable
    data class Cases(val grading: List<GradingCase>, val passMark: List<PassMarkCase>, val trapRadar: List<TrapCase>)

    private val cases: Cases = json.decodeFromString(Cases.serializer(), File(System.getProperty("robin.coreCases")).readText())

    private companion object {
        val json = Json { ignoreUnknownKeys = true }
    }

    @Test
    fun grading() {
        for (c in cases.grading) {
            assertEquals(c.name, c.correct, Grader.isCorrect(c.type, c.answer, c.response))
        }
    }

    @Test
    fun passMark() {
        for (c in cases.passMark) {
            val rules = ExamRules(questionCount = c.questionCount, durationMinutes = 45, passMark = c.passMark)
            assertEquals(c.name, c.passed, Grader.passed(rules, c.total, c.score))
        }
    }

    @Test
    fun trapRadar() {
        for (c in cases.trapRadar) {
            assertEquals(c.text, c.traps, TrapRadar.traps(c.text).map { it.id })
        }
    }
}
