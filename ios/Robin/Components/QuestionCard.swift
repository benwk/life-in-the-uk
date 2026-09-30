import RobinCore
import SwiftUI

/// Renders a question and collects a response. Used by practice (with
/// translations, trap radar and feedback) and by exam mode (English only).
struct QuestionCard: View {
    let question: Question
    @Binding var response: [String]
    let locale: String
    let examMode: Bool
    /// When true, shows correct/incorrect colouring and the explanation.
    let revealed: Bool

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text(examMode ? AttributedString(question.prompt.english) : highlightedPrompt)
                .font(.title3.weight(.semibold))
            if !examMode, let native = question.prompt.translation(locale) {
                Text(native).font(.subheadline).foregroundStyle(.secondary)
            }
            switch question.type {
            case .multiple: Text("Choose TWO answers").font(.caption.weight(.bold))
            case .order: Text("Tap in order, earliest first").font(.caption.weight(.bold))
            default: EmptyView()
            }

            if question.type == .match {
                // TODO: drag-to-match mini game (docs/product/05-practice-journey.md).
                Text("Match Up is coming soon.")
            } else {
                ForEach(question.options) { option in
                    optionButton(option)
                }
                if question.type == .order && !response.isEmpty && !revealed {
                    Button("Start again") { response = [] }
                }
            }

            if revealed {
                Text(question.explanation.localized(examMode ? "en" : locale))
                    .font(.callout)
                    .padding(.top, 4)
            }
        }
    }

    private func optionButton(_ option: Option) -> some View {
        let index = response.firstIndex(of: option.id)
        let selected = index != nil
        let correct = question.answer.contains(option.id)
        let border: Color =
            if revealed && correct { RobinColors.racingGreen }
            else if revealed && selected { RobinColors.amber }
            else if selected { RobinColors.robin }
            else { Color.secondary.opacity(0.4) }

        return Button {
            if !revealed { toggle(option.id) }
        } label: {
            VStack(alignment: .leading, spacing: 2) {
                let prefix = question.type == .order && index != nil ? "\(index! + 1). " : ""
                Text(prefix + option.text.english).font(.body)
                if !examMode, let native = option.text.translation(locale) {
                    Text(native).font(.footnote).foregroundStyle(.secondary)
                }
            }
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(14)
            .overlay(
                RoundedRectangle(cornerRadius: 12)
                    .stroke(border, lineWidth: selected || (revealed && correct) ? 3 : 1)
            )
        }
        .buttonStyle(.plain)
    }

    private func toggle(_ id: String) {
        switch question.type {
        case .multiple:
            if let i = response.firstIndex(of: id) {
                response.remove(at: i)
            } else {
                response = Array((response + [id]).suffix(question.answer.count))
            }
        case .order:
            if !response.contains(id) { response.append(id) }
        default:
            response = [id]
        }
    }

    private var highlightedPrompt: AttributedString {
        let text = question.prompt.english
        var result = AttributedString()
        var cursor = text.startIndex
        for highlight in TrapRadar.highlights(in: text) where highlight.range.lowerBound >= cursor {
            result += AttributedString(String(text[cursor..<highlight.range.lowerBound]))
            var trap = AttributedString(String(text[highlight.range]))
            trap.backgroundColor = RobinColors.amber.opacity(0.35)
            trap.font = .title3.bold()
            result += trap
            cursor = highlight.range.upperBound
        }
        result += AttributedString(String(text[cursor...]))
        return result
    }
}
