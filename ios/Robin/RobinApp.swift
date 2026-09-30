import SwiftUI

@main
struct RobinApp: App {
    @State private var state = AppState()

    var body: some Scene {
        WindowGroup {
            RootView()
                .environment(state)
                .tint(RobinColors.robin)
        }
    }
}

/// The five tabs from docs/product/03-experience-map.md.
struct RootView: View {
    var body: some View {
        TabView {
            NavigationStack { TodayView() }
                .tabItem { Label("Today", systemImage: "cup.and.saucer") }
            NavigationStack { JourneyView() }
                .tabItem { Label("Journey", systemImage: "map") }
            NavigationStack { PracticeView(stationId: nil) }
                .tabItem { Label("Practice", systemImage: "brain.head.profile") }
            NavigationStack { TestDayView() }
                .tabItem { Label("Test Day", systemImage: "ticket") }
            NavigationStack { PassportView() }
                .tabItem { Label("Passport", systemImage: "person.text.rectangle") }
        }
    }
}
