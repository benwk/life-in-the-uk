import Foundation

/// A mock exam in progress: fixed questions, the learner's responses and flags.
public struct MockExam: Sendable {
    public let questions: [Question]
    public let rules: ExamRules
    public let durationMinutes: Int
    private var responses: [String: [String]] = [:]
    private var flagged: Set<String> = []

    public init(questions: [Question], rules: ExamRules, durationMinutes: Int) {
        self.questions = questions
        self.rules = rules
        self.durationMinutes = durationMinutes
    }

    public mutating func answer(_ questionId: String, response: [String]) {
        responses[questionId] = response
    }

    public func response(_ questionId: String) -> [String] {
        responses[questionId] ?? []
    }

    public mutating func toggleFlag(_ questionId: String) {
        if flagged.remove(questionId) == nil { flagged.insert(questionId) }
    }

    public func isFlagged(_ questionId: String) -> Bool {
        flagged.contains(questionId)
    }

    public var answeredCount: Int {
        questions.filter { !response($0.id).isEmpty }.count
    }

    public func result() -> ExamResult {
        let wrong = questions.filter { !Grader.isCorrect($0, response: response($0.id)) }
        let score = questions.count - wrong.count
        return ExamResult(
            score: score,
            total: questions.count,
            requiredToPass: Grader.requiredToPass(rules, total: questions.count),
            passed: Grader.passed(rules, total: questions.count, score: score),
            wrongQuestionIds: wrong.map(\.id)
        )
    }

    /// Picks `count` exam-eligible questions, spreading them across lines
    /// round-robin so every chapter is represented. If the bank is smaller
    /// than `count`, every eligible question is used.
    public static func build<G: RandomNumberGenerator>(
        bundle: ContentBundle,
        count: Int? = nil,
        durationMinutes: Int? = nil,
        using generator: inout G
    ) -> MockExam {
        let count = count ?? bundle.meta.exam.questionCount
        var lineOf: [String: String] = [:]
        for line in bundle.lines {
            for station in line.stations { lineOf[station.id] = line.id }
        }
        let eligible = bundle.questions.filter { $0.examEligible && $0.type.isExamType }
        var pools = Dictionary(grouping: eligible) { lineOf[$0.stationId] ?? "" }
            .keys.sorted()
            .map { key in eligible.filter { (lineOf[$0.stationId] ?? "") == key }.shuffled(using: &generator) }
            .shuffled(using: &generator)

        var picked: [Question] = []
        while picked.count < count && pools.contains(where: { !$0.isEmpty }) {
            for i in pools.indices where picked.count < count && !pools[i].isEmpty {
                picked.append(pools[i].removeFirst())
            }
        }
        return MockExam(
            questions: picked.shuffled(using: &generator),
            rules: bundle.meta.exam,
            durationMinutes: durationMinutes ?? bundle.meta.exam.durationMinutes
        )
    }

    public static func build(bundle: ContentBundle, count: Int? = nil, durationMinutes: Int? = nil) -> MockExam {
        var generator = SystemRandomNumberGenerator()
        return build(bundle: bundle, count: count, durationMinutes: durationMinutes, using: &generator)
    }
}

public struct ExamResult: Sendable, Equatable {
    public let score: Int
    public let total: Int
    public let requiredToPass: Int
    public let passed: Bool
    public let wrongQuestionIds: [String]
}
