import Foundation

public enum Grader {
    /// `response` is the option ids the learner chose. For `.order` it is the
    /// sequence they built; for `.match` it is, for each option in order, the
    /// id of the option whose matchText they paired with it.
    public static func isCorrect(type: QuestionType, answer: [String], response: [String]) -> Bool {
        switch type {
        case .order, .match:
            return response == answer
        default:
            return response.count == answer.count && Set(response) == Set(answer)
        }
    }

    public static func isCorrect(_ question: Question, response: [String]) -> Bool {
        isCorrect(type: question.type, answer: question.answer, response: response)
    }

    /// Correct answers needed to pass a mock of `total` questions. The real
    /// test's pass mark is scaled for shorter mocks and rounded up, so a mini
    /// mock is never easier than the real thing.
    public static func requiredToPass(_ rules: ExamRules, total: Int) -> Int {
        if total == rules.questionCount { return rules.passMark }
        return Int((Double(rules.passMark) * Double(total) / Double(rules.questionCount)).rounded(.up))
    }

    public static func passed(_ rules: ExamRules, total: Int, score: Int) -> Bool {
        score >= requiredToPass(rules, total: total)
    }
}
