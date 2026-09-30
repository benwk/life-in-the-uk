package uk.robin.core

/** Finds wording in a question that commonly causes mistakes (see docs/product/07-language-experience.md). */
object TrapRadar {
    enum class Trap(val id: String) {
        NEGATION("negation"),
        CHOOSE_TWO("choose-two"),
        ABSOLUTE("absolute"),
    }

    data class Highlight(val trap: Trap, val start: Int, val end: Int)

    private val patterns = listOf(
        Trap.NEGATION to Regex("""\b(not|isn't|aren't|wasn't|weren't|doesn't|don't|didn't)\b""", RegexOption.IGNORE_CASE),
        // Only upper-case TWO is the "choose two answers" instruction; "two thirds" is not a trap.
        Trap.CHOOSE_TWO to Regex("""\bTWO\b"""),
        Trap.ABSOLUTE to Regex("""\b(always|never|only)\b""", RegexOption.IGNORE_CASE),
    )

    fun highlights(text: String): List<Highlight> =
        patterns
            .flatMap { (trap, regex) -> regex.findAll(text).map { Highlight(trap, it.range.first, it.range.last + 1) } }
            .sortedBy { it.start }

    /** Distinct traps in order of first appearance. */
    fun traps(text: String): List<Trap> = highlights(text).map { it.trap }.distinct()
}
