package uk.robin.core

import kotlin.random.Random

/**
 * Picks the daily Tea Break questions.
 *
 * MVP rule: recent mistakes first, then questions the learner has not seen,
 * then anything else. Will be replaced by FSRS scheduling per fact
 * (see docs/product/05-practice-journey.md).
 */
object TeaBreak {
    fun pick(
        questions: List<Question>,
        count: Int,
        mistakes: Set<String> = emptySet(),
        seen: Set<String> = emptySet(),
        random: Random = Random.Default,
    ): List<Question> {
        val (wrong, rest) = questions.partition { it.id in mistakes }
        val (unseen, others) = rest.partition { it.id !in seen }
        return (wrong.shuffled(random) + unseen.shuffled(random) + others.shuffled(random)).take(count)
    }
}
