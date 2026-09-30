import Foundation

/// Picks the daily Tea Break questions.
///
/// MVP rule: recent mistakes first, then questions the learner has not seen,
/// then anything else. Will be replaced by FSRS scheduling per fact
/// (see docs/product/05-practice-journey.md).
public enum TeaBreak {
    public static func pick<G: RandomNumberGenerator>(
        from questions: [Question],
        count: Int,
        mistakes: Set<String> = [],
        seen: Set<String> = [],
        using generator: inout G
    ) -> [Question] {
        let wrong = questions.filter { mistakes.contains($0.id) }
        let rest = questions.filter { !mistakes.contains($0.id) }
        let unseen = rest.filter { !seen.contains($0.id) }
        let others = rest.filter { seen.contains($0.id) }
        let ordered = wrong.shuffled(using: &generator) + unseen.shuffled(using: &generator) + others.shuffled(using: &generator)
        return Array(ordered.prefix(count))
    }

    public static func pick(from questions: [Question], count: Int, mistakes: Set<String> = [], seen: Set<String> = []) -> [Question] {
        var generator = SystemRandomNumberGenerator()
        return pick(from: questions, count: count, mistakes: mistakes, seen: seen, using: &generator)
    }
}
