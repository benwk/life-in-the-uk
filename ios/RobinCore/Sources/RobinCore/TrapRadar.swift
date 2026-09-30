import Foundation

/// Finds wording in a question that commonly causes mistakes (see docs/product/07-language-experience.md).
public enum TrapRadar {
    public enum Trap: String, Sendable {
        case negation
        case chooseTwo = "choose-two"
        case absolute
    }

    public struct Highlight: Sendable {
        public let trap: Trap
        public let range: Range<String.Index>
    }

    private static let patterns: [(Trap, NSRegularExpression)] = [
        (.negation, try! NSRegularExpression(pattern: #"\b(not|isn't|aren't|wasn't|weren't|doesn't|don't|didn't)\b"#, options: [.caseInsensitive])),
        // Only upper-case TWO is the "choose two answers" instruction; "two thirds" is not a trap.
        (.chooseTwo, try! NSRegularExpression(pattern: #"\bTWO\b"#)),
        (.absolute, try! NSRegularExpression(pattern: #"\b(always|never|only)\b"#, options: [.caseInsensitive])),
    ]

    public static func highlights(in text: String) -> [Highlight] {
        let whole = NSRange(text.startIndex..., in: text)
        return patterns
            .flatMap { trap, regex in
                regex.matches(in: text, range: whole).compactMap { match in
                    Range(match.range, in: text).map { Highlight(trap: trap, range: $0) }
                }
            }
            .sorted { $0.range.lowerBound < $1.range.lowerBound }
    }

    /// Distinct traps in order of first appearance.
    public static func traps(in text: String) -> [Trap] {
        var result: [Trap] = []
        for highlight in highlights(in: text) where !result.contains(highlight.trap) {
            result.append(highlight.trap)
        }
        return result
    }
}
