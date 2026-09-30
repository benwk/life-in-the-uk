import RobinCore
import SwiftUI

/// A station: swipeable story cards, then practice and the passport stamp.
struct StationView: View {
    @Environment(AppState.self) private var state
    let station: Station

    var body: some View {
        let facts = state.bundle.facts(for: station.id)

        VStack(alignment: .leading, spacing: 8) {
            Text(station.title.localized(state.learnerLocale)).font(.title2.bold())
            Text(station.title.english).font(.caption).foregroundStyle(.secondary)

            TabView {
                ForEach(facts) { fact in
                    storyCard(fact).padding(.bottom, 40)
                }
                endOfLine.padding(.bottom, 40)
            }
            .tabViewStyle(.page)
            .indexViewStyle(.page(backgroundDisplayMode: .always))

            if facts.isEmpty {
                Text("Story cards for this station are still being written.")
                    .font(.caption)
                    .foregroundStyle(.secondary)
            }
        }
        .padding()
        .background(RobinColors.paper)
        .navigationBarTitleDisplayMode(.inline)
    }

    private func storyCard(_ fact: Fact) -> some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 12) {
                if let year = fact.year {
                    Text(String(year)).font(.system(size: 56, weight: .bold, design: .serif))
                }
                BilingualText(text: fact.text, locale: state.learnerLocale, mode: state.lensMode, font: .title3)
                if let hook = fact.memoryHook?[state.learnerLocale] {
                    Text("💡 \(hook)").font(.callout)
                }
                if let note = fact.examNote {
                    Text("⚠️ \(note.localized(state.learnerLocale))").font(.footnote)
                }
            }
            .padding(20)
        }
        .background(.background, in: RoundedRectangle(cornerRadius: 20))
    }

    private var endOfLine: some View {
        VStack(spacing: 16) {
            Text("🛂 End of the line").font(.title2.bold())
            NavigationLink("Platform quiz") { PracticeView(stationId: station.id) }
                .buttonStyle(.borderedProminent)
            let stamped = state.stamps.contains(station.id)
            Button(stamped ? "Stamp collected" : "Collect stamp") { state.collectStamp(station.id) }
                .buttonStyle(.bordered)
                .disabled(stamped)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}
