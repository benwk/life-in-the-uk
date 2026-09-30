import RobinCore
import SwiftUI

/// English + learner-language text following the Language Lens.
/// Tapping flips between the two languages (docs/product/07-language-experience.md).
struct BilingualText: View {
    let text: LocalizedText
    let locale: String
    let mode: LensMode
    var font: Font = .body

    @State private var flipped = false

    var body: some View {
        Group {
            if let native = text.translation(locale) {
                let nativeOnTop = (mode == .nativeFirst) != flipped
                if mode == .englishFirst {
                    Text(flipped ? native : text.english).font(font)
                } else {
                    VStack(alignment: .leading, spacing: 4) {
                        Text(nativeOnTop ? native : text.english).font(font)
                        Text(nativeOnTop ? text.english : native)
                            .font(.subheadline)
                            .foregroundStyle(.secondary)
                    }
                }
            } else {
                Text(text.english).font(font)
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .contentShape(Rectangle())
        .onTapGesture {
            withAnimation(.easeInOut(duration: 0.25)) { flipped.toggle() }
        }
    }
}
