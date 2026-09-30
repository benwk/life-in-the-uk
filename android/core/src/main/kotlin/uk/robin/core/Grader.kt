package uk.robin.core

import kotlin.math.ceil

object Grader {
    /**
     * [response] is the option ids the learner chose. For ORDER it is the
     * sequence they built; for MATCH it is, for each option in order, the id
     * of the option whose matchText they paired with it.
     */
    fun isCorrect(type: QuestionType, answer: List<String>, response: List<String>): Boolean = when (type) {
        QuestionType.ORDER, QuestionType.MATCH -> response == answer
        else -> response.size == answer.size && response.toSet() == answer.toSet()
    }

    fun isCorrect(question: Question, response: List<String>): Boolean =
        isCorrect(question.type, question.answer, response)

    /**
     * Correct answers needed to pass a mock of [total] questions. The real
     * test's pass mark is scaled for shorter mocks and rounded up, so a mini
     * mock is never easier than the real thing.
     */
    fun requiredToPass(rules: ExamRules, total: Int): Int =
        if (total == rules.questionCount) {
            rules.passMark
        } else {
            ceil(rules.passMark.toDouble() * total / rules.questionCount).toInt()
        }

    fun passed(rules: ExamRules, total: Int, score: Int): Boolean = score >= requiredToPass(rules, total)
}
