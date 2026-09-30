import RobinCore
import SwiftUI

struct TodayView: View {
    @Environment(AppState.self) private var state

    var body: some View {
        let bundle = state.bundle
        let next = bundle.stations.first { !state.stamps.contains($0.id) }

        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                Text("🐦 Hello!").font(.largeTitle.bold())

                card {
                    Text("☕ Today's Tea Break").font(.title2.bold())
                    Text("\(bundle.meta.dailyTeaBreak.questionCount) questions · about 3 minutes")
                    NavigationLink("Start") { PracticeView(stationId: nil) }
                        .buttonStyle(.borderedProminent)
                }

                if let next {
                    card {
                        Text("🚉 Next stop").font(.title2.bold())
                        Text(next.title.localized(state.learnerLocale))
                        Text(next.title.english).font(.caption).foregroundStyle(.secondary)
                        NavigationLink("Continue the journey") { StationView(station: next) }
                            .buttonStyle(.bordered)
                    }
                }

                card {
                    Text("🎫 Test Day").font(.title2.bold())
                    Text("Stamps: \(state.stamps.count) / \(bundle.stations.count)")
                }
            }
            .padding()
        }
        .background(RobinColors.paper)
        .navigationTitle("Today")
        .toolbar(.hidden, for: .navigationBar)
    }

    private func card<Content: View>(@ViewBuilder _ content: () -> Content) -> some View {
        VStack(alignment: .leading, spacing: 8, content: content)
            .padding()
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(.background, in: RoundedRectangle(cornerRadius: 16))
    }
}
