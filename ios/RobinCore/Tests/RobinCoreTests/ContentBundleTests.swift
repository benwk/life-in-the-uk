import Foundation
import XCTest
@testable import RobinCore

/// Decodes the real bundle, so model drift fails the tests.
/// Run `python3 tools/build_content.py` first (CI does).
final class ContentBundleTests: XCTestCase {
    private var bundle: ContentBundle!

    override func setUpWithError() throws {
        let url = repoRoot.appendingPathComponent("build/content.json")
        guard FileManager.default.fileExists(atPath: url.path) else {
            throw XCTSkip("build/content.json missing; run python3 tools/build_content.py")
        }
        bundle = try ContentBundle.decode(Data(contentsOf: url))
    }

    func testDecodesBundle() {
        XCTAssertEqual(bundle.meta.exam.questionCount, 24)
        XCTAssertFalse(bundle.stations.isEmpty)
        XCTAssertFalse(bundle.questions.isEmpty)
        for q in bundle.questions {
            XCTAssertTrue(Grader.isCorrect(q, response: q.answer), "\(q.id) answer should grade as correct")
        }
    }

    func testLocalizedFallsBackToEnglish() {
        let text: LocalizedText = ["en": "Hello", "zh-Hans": "你好"]
        XCTAssertEqual(text.localized("zh-Hans"), "你好")
        XCTAssertEqual(text.localized("uk"), "Hello")
        XCTAssertNil(text.translation("en"))
    }

    func testMockExamUsesOnlyExamQuestionsAndSpreadsAcrossLines() {
        var rng = SplitMix64(state: 1)
        let exam = MockExam.build(bundle: bundle, count: 5, using: &rng)
        XCTAssertEqual(exam.questions.count, 5)
        XCTAssertTrue(exam.questions.allSatisfy { $0.examEligible && $0.type.isExamType })
        XCTAssertEqual(Set(exam.questions.compactMap { bundle.line(containing: $0.stationId)?.id }).count, 5)
    }

    func testMockExamScoresResponses() {
        var rng = SplitMix64(state: 2)
        var exam = MockExam.build(bundle: bundle, using: &rng)
        for (i, q) in exam.questions.enumerated() where i % 2 == 0 {
            exam.answer(q.id, response: q.answer)
        }
        let result = exam.result()
        XCTAssertEqual(result.total, exam.questions.count)
        XCTAssertEqual(result.score, (exam.questions.count + 1) / 2)
        XCTAssertEqual(result.wrongQuestionIds.count, exam.questions.count - result.score)
    }

    func testTeaBreakPutsMistakesFirst() {
        var rng = SplitMix64(state: 3)
        let mistake = bundle.questions.last!.id
        let picked = TeaBreak.pick(from: bundle.questions, count: 3, mistakes: [mistake], using: &rng)
        XCTAssertEqual(picked.count, 3)
        XCTAssertEqual(picked.first?.id, mistake)
    }
}
