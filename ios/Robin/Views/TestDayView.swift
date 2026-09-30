import RobinCore
import SwiftUI

struct TestDayView: View {
    @Environment(AppState.self) private var state
    @State private var activeMock: MockKind?

    enum MockKind: String, Identifiable {
        case full, mini
        var id: String { rawValue }
    }

    var body: some View {
        let meta = state.bundle.meta
        // Simplified readiness until FSRS mastery exists: share of stations stamped.
        let readiness = Double(state.stamps.count) / Double(max(state.bundle.stations.count, 1))

        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                VStack(alignment: .leading, spacing: 8) {
                    Text("Readiness \(Int(readiness * 100))%").font(.title2.bold())
                    ProgressView(value: readiness)
                    Text("This is an estimate, not a guarantee.").font(.caption).foregroundStyle(.secondary)
                }
                .padding()
                .background(.background, in: RoundedRectangle(cornerRadius: 16))

                Button("Full mock · \(meta.exam.questionCount) questions · \(meta.exam.durationMinutes) min") {
                    activeMock = .full
                }
                .buttonStyle(.borderedProminent)

                Button("Mini mock · \(meta.miniMock.questionCount) questions · \(meta.miniMock.durationMinutes) min") {
                    activeMock = .mini
                }
                .buttonStyle(.bordered)

                Text("Pass mark: \(meta.exam.passMark) / \(meta.exam.questionCount)")
                    .font(.caption)
                    .foregroundStyle(.secondary)
            }
            .padding()
        }
        .background(RobinColors.paper)
        .navigationTitle("Test Day")
        // Full screen hides the tab bar: exam mode, like the real test.
        .fullScreenCover(item: $activeMock) { kind in
            MockExamView(bundle: state.bundle, mini: kind == .mini)
                .environment(state)
        }
    }
}
