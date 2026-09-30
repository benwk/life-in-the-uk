package uk.robin.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import kotlin.random.Random

/** Decodes the real bundle from tools/build_content.py, so model drift fails the build. */
class ContentBundleTest {
    private val bundle = ContentBundle.decode(File(System.getProperty("robin.contentBundle")).readText())

    @Test
    fun decodesBundle() {
        assertEquals(24, bundle.meta.exam.questionCount)
        assertTrue(bundle.stations.isNotEmpty())
        assertTrue(bundle.questions.isNotEmpty())
        for (q in bundle.questions) {
            assertTrue("${q.id} answer should grade as correct", Grader.isCorrect(q, q.answer))
        }
    }

    @Test
    fun localizedFallsBackToEnglish() {
        val text: LocalizedText = mapOf("en" to "Hello", "zh-Hans" to "你好")
        assertEquals("你好", text.localized("zh-Hans"))
        assertEquals("Hello", text.localized("uk"))
        assertEquals(null, text.translation("en"))
    }

    @Test
    fun mockExamUsesOnlyExamQuestionsAndSpreadsAcrossLines() {
        val exam = MockExam.build(bundle, count = 5, random = Random(1))
        assertEquals(5, exam.questions.size)
        assertTrue(exam.questions.all { it.examEligible && it.type.isExamType })
        assertEquals(5, exam.questions.map { bundle.line(it.stationId)!!.id }.distinct().size)
    }

    @Test
    fun mockExamScoresResponses() {
        val exam = MockExam.build(bundle, random = Random(2))
        exam.questions.forEachIndexed { i, q -> if (i % 2 == 0) exam.answer(q.id, q.answer) }
        val result = exam.result()
        assertEquals(exam.questions.size, result.total)
        assertEquals((exam.questions.size + 1) / 2, result.score)
        assertEquals(exam.questions.size - result.score, result.wrongQuestionIds.size)
    }

    @Test
    fun teaBreakPutsMistakesFirst() {
        val mistake = bundle.questions.last().id
        val picked = TeaBreak.pick(bundle.questions, count = 3, mistakes = setOf(mistake), random = Random(3))
        assertEquals(3, picked.size)
        assertEquals(mistake, picked.first().id)
    }
}
