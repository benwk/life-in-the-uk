import RobinCore
import SwiftUI

/// Tea Break (no station) or practice for one station.
struct PracticeView: View {
    @Environment(AppState.self) private var state
    let stationId: String?

    @State private var questions: [Question]?
    @State private var index = 0
    @State private var response: [String] = []
    @State private var revealed = false
    @State private var correctCount = 0

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                if let questions {
                    content(questions)
                }
            }
            .padding()
        }
        .navigationTitle(stationId == nil ? "Tea Break" : "Practice")
        .onAppear {
            if questions == nil { questions = pickQuestions() }
        }
    }

    @ViewBuilder
    private func content(_ questions: [Question]) -> some View {
        if questions.isEmpty {
            Text("No questions here yet.")
        } else if index == questions.count {
            Text("☕ Tea finished!").font(.largeTitle.bold())
            Text("\(correctCount) / \(questions.count) correct")
            Button("Another cup") {
                self.questions = pickQuestions()
                index = 0
                correctCount = 0
            }
        } else {
            let question = questions[index]
            ProgressView(value: Double(index), total: Double(questions.count))
            QuestionCard(
                question: question,
                response: $response,
                locale: state.learnerLocale,
                examMode: false,
                revealed: revealed
            )
            if revealed {
                Button("Next") {
                    index += 1
                    response = []
                    revealed = false
                }
                .buttonStyle(.borderedProminent)
            } else {
                Button("Check") {
                    let correct = Grader.isCorrect(question, response: response)
                    if correct { correctCount += 1 }
                    state.recordAnswer(question.id, correct: correct)
                    revealed = true
                }
                .buttonStyle(.borderedProminent)
                .disabled(response.count != question.answer.count)
            }
        }
    }

    private func pickQuestions() -> [Question] {
        let bundle = state.bundle
        let pool = (stationId.map(bundle.questions(for:)) ?? bundle.questions).filter { $0.type != .match }
        guard stationId == nil else { return pool.shuffled() }
        return TeaBreak.pick(
            from: pool,
            count: bundle.meta.dailyTeaBreak.questionCount,
            mistakes: state.mistakes,
            seen: state.seen
        )
    }
}
