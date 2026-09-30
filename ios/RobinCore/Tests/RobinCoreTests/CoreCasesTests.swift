import Foundation
import XCTest
@testable import RobinCore

/// Runs content/fixtures/core-cases.json, the cases shared with android/core.
final class CoreCasesTests: XCTestCase {
    struct GradingCase: Decodable { let name: String; let type: QuestionType; let answer: [String]; let response: [String]; let correct: Bool }
    struct PassMarkCase: Decodable { let name: String; let questionCount: Int; let passMark: Int; let total: Int; let score: Int; let passed: Bool }
    struct TrapCase: Decodable { let text: String; let traps: [String] }
    struct Cases: Decodable { let grading: [GradingCase]; let passMark: [PassMarkCase]; let trapRadar: [TrapCase] }

    private var cases: Cases!

    override func setUpWithError() throws {
        let data = try Data(contentsOf: repoRoot.appendingPathComponent("content/fixtures/core-cases.json"))
        cases = try JSONDecoder().decode(Cases.self, from: data)
    }

    func testGrading() {
        for c in cases.grading {
            XCTAssertEqual(Grader.isCorrect(type: c.type, answer: c.answer, response: c.response), c.correct, c.name)
        }
    }

    func testPassMark() {
        for c in cases.passMark {
            let rules = ExamRules(questionCount: c.questionCount, durationMinutes: 45, passMark: c.passMark)
            XCTAssertEqual(Grader.passed(rules, total: c.total, score: c.score), c.passed, c.name)
        }
    }

    func testTrapRadar() {
        for c in cases.trapRadar {
            XCTAssertEqual(TrapRadar.traps(in: c.text).map(\.rawValue), c.traps, c.text)
        }
    }
}
