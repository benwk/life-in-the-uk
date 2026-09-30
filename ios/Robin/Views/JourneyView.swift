import RobinCore
import SwiftUI

/// Placeholder for the railway line map (docs/product/04-learning-journey.md):
/// each line is a coloured rail with its stations. The real map is waiting on the design.
struct JourneyView: View {
    @Environment(AppState.self) private var state

    var body: some View {
        List {
            ForEach(state.bundle.lines) { line in
                let color = RobinColors.line(line.color)
                Section {
                    ForEach(line.stations) { station in
                        NavigationLink {
                            StationView(station: station)
                        } label: {
                            stationRow(station, color: color)
                        }
                    }
                } header: {
                    VStack(alignment: .leading) {
                        Text(line.title.localized(state.learnerLocale)).font(.headline).foregroundStyle(color)
                        Text(line.title.english).font(.caption)
                    }
                }
            }
        }
        .navigationTitle("Journey")
    }

    private func stationRow(_ station: Station, color: Color) -> some View {
        let stamped = state.stamps.contains(station.id)
        return HStack(spacing: 12) {
            ZStack {
                Rectangle().fill(color).frame(width: 6, height: 44)
                Circle()
                    .fill(stamped ? color : Color(.systemBackground))
                    .overlay(Circle().stroke(color, lineWidth: 3))
                    .frame(width: 20, height: 20)
            }
            VStack(alignment: .leading) {
                Text(station.title.localized(state.learnerLocale))
                Text("\(station.title.english) · \(station.minutes) min\(stamped ? " · 🛂" : "")")
                    .font(.caption)
                    .foregroundStyle(.secondary)
            }
        }
    }
}
