import RobinCore
import SwiftUI

struct PassportView: View {
    @Environment(AppState.self) private var state

    var body: some View {
        let bundle = state.bundle
        Form {
            Section("\(state.stamps.count) / \(bundle.stations.count) stamps") {
                ForEach(bundle.stations.filter { state.stamps.contains($0.id) }) { station in
                    Text("🔖 \(station.title.localized(state.learnerLocale))")
                }
            }
            Section("Learning language") {
                Picker("Learning language", selection: Binding(
                    get: { state.learnerLocale },
                    set: { state.updateLearnerLocale($0) }
                )) {
                    ForEach(bundle.meta.locales.filter { $0.code != "en" }) { locale in
                        Text(locale.name).tag(locale.code)
                    }
                }
                .pickerStyle(.inline)
                .labelsHidden()
            }
            Section("Language Lens") {
                Picker("Language Lens", selection: Binding(
                    get: { state.lensMode },
                    set: { state.updateLensMode($0) }
                )) {
                    ForEach(LensMode.allCases) { mode in
                        Text(mode.label).tag(mode)
                    }
                }
                .pickerStyle(.segmented)
            }
        }
        .navigationTitle("Passport")
    }
}
