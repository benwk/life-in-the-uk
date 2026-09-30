package uk.robin.core

import kotlin.random.Random

/** A mock exam in progress: fixed questions, the learner's responses and flags. */
class MockExam(
    val questions: List<Question>,
    val rules: ExamRules,
    val durationMinutes: Int,
) {
    private val responses = mutableMapOf<String, List<String>>()
    private val flagged = mutableSetOf<String>()

    fun answer(questionId: String, response: List<String>) {
        responses[questionId] = response
    }

    fun response(questionId: String): List<String> = responses[questionId].orEmpty()

    fun toggleFlag(questionId: String) {
        if (!flagged.add(questionId)) flagged.remove(questionId)
    }

    fun isFlagged(questionId: String): Boolean = questionId in flagged

    val answeredCount: Int get() = questions.count { responses[it.id].orEmpty().isNotEmpty() }

    fun result(): ExamResult {
        val wrong = questions.filterNot { Grader.isCorrect(it, response(it.id)) }
        val score = questions.size - wrong.size
        return ExamResult(
            score = score,
            total = questions.size,
            requiredToPass = Grader.requiredToPass(rules, questions.size),
            passed = Grader.passed(rules, questions.size, score),
            wrongQuestionIds = wrong.map { it.id },
        )
    }

    companion object {
        /**
         * Picks [count] exam-eligible questions, spreading them across lines
         * round-robin so every chapter is represented. If the bank is smaller
         * than [count], every eligible question is used.
         */
        fun build(
            bundle: ContentBundle,
            count: Int = bundle.meta.exam.questionCount,
            durationMinutes: Int = bundle.meta.exam.durationMinutes,
            random: Random = Random.Default,
        ): MockExam {
            val lineOf = bundle.lines.flatMap { line -> line.stations.map { it.id to line.id } }.toMap()
            val byLine = bundle.questions
                .filter { it.examEligible && it.type.isExamType }
                .groupBy { lineOf[it.stationId] }
                .values
                .map { it.shuffled(random).toMutableList() }
                .shuffled(random)

            val picked = mutableListOf<Question>()
            while (picked.size < count && byLine.any { it.isNotEmpty() }) {
                for (pool in byLine) {
                    if (picked.size == count) break
                    pool.removeFirstOrNull()?.let(picked::add)
                }
            }
            return MockExam(picked.shuffled(random), bundle.meta.exam, durationMinutes)
        }
    }
}

data class ExamResult(
    val score: Int,
    val total: Int,
    val requiredToPass: Int,
    val passed: Boolean,
    val wrongQuestionIds: List<String>,
)
