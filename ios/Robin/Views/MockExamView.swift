import RobinCore
import SwiftUI

/// Exam mode: English only, no mascot, calm timer (docs/product/06-mock-exam-journey.md).
struct MockExamView: View {
    @Environment(AppState.self) private var state
    @Environment(\.dismiss) private var dismiss

    @State private var exam: MockExam
    @State private var index = 0
    @State private var secondsLeft: Int
    @State private var result: ExamResult?

    init(bundle: ContentBundle, mini: Bool) {
        let meta = bundle.meta
        let exam = mini
            ? MockExam.build(bundle: bundle, count: meta.miniMock.questionCount, durationMinutes: meta.miniMock.durationMinutes)
            : MockExam.build(bundle: bundle)
        _exam = State(initialValue: exam)
        _secondsLeft = State(initialValue: exam.durationMinutes * 60)
    }

    var body: some View {
        NavigationStack {
            Group {
                if let result {
                    resultView(result)
                } else if exam.questions.isEmpty {
                    Text("No exam questions yet.")
                } else {
                    questionView(exam.questions[index])
                }
            }
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button(result == nil ? "Quit" : "Done") { dismiss() }
                }
            }
        }
        .task {
            while secondsLeft > 0 && result == nil {
                try? await Task.sleep(for: .seconds(1))
                secondsLeft -= 1
            }
            if result == nil { result = exam.result() }
        }
    }

    private func questionView(_ question: Question) -> some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                HStack {
                    Text("Question \(index + 1) of \(exam.questions.count)")
                    Spacer()
                    Text(String(format: "%d:%02d", secondsLeft / 60, secondsLeft % 60)).monospacedDigit()
                }
                .font(.subheadline)

                QuestionCard(
                    question: question,
                    response: Binding(
                        get: { exam.response(question.id) },
                        set: { exam.answer(question.id, response: $0) }
                    ),
                    locale: "en",
                    examMode: true,
                    revealed: false
                )

                Button(exam.isFlagged(question.id) ? "🚩 Flagged" : "Flag for review") {
                    exam.toggleFlag(question.id)
                }

                HStack {
                    Button("Previous") { index -= 1 }.disabled(index == 0)
                    Spacer()
                    if index < exam.questions.count - 1 {
                        Button("Next") { index += 1 }.buttonStyle(.borderedProminent)
                    } else {
                        Button("Submit") { result = exam.result() }.buttonStyle(.borderedProminent)
                    }
                }
            }
            .padding()
        }
    }

    private func resultView(_ result: ExamResult) -> some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 12) {
                Text("\(result.score) / \(result.total)").font(.system(size: 56, weight: .bold, design: .serif))
                Text(result.passed ? "PASSED ✅" : "Needed \(result.requiredToPass) — keep going!").font(.title2)
                // Review: translations come back (docs/product/06-mock-exam-journey.md).
                ForEach(exam.questions.filter { result.wrongQuestionIds.contains($0.id) }) { q in
                    VStack(alignment: .leading, spacing: 4) {
                        Text("• \(q.prompt.english)").font(.body.bold())
                        Text(q.explanation.localized(state.learnerLocale)).font(.callout)
                    }
                }
            }
            .padding()
        }
    }
}
