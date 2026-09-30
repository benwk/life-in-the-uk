package uk.robin.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import uk.robin.app.AppState
import uk.robin.app.ui.journey.JourneyScreen
import uk.robin.app.ui.journey.StationScreen
import uk.robin.app.ui.passport.PassportScreen
import uk.robin.app.ui.practice.PracticeScreen
import uk.robin.app.ui.testday.MockExamScreen
import uk.robin.app.ui.testday.TestDayScreen
import uk.robin.app.ui.today.TodayScreen

/** The five tabs from docs/product/03-experience-map.md. Emoji stand in for icons until the design lands. */
private enum class Tab(val route: String, val icon: String, val label: String) {
    Today("today", "☕", "Today"),
    Journey("journey", "🗺️", "Journey"),
    Practice("practice", "🧠", "Practice"),
    TestDay("testday", "🎫", "Test Day"),
    Passport("passport", "🛂", "Passport"),
}

@Composable
fun RobinApp(state: AppState) {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val route = backStack?.destination?.route
    val inExam = route?.startsWith("mock/") == true

    Scaffold(
        bottomBar = {
            // Exam mode hides the tabs, like the real test.
            if (!inExam) {
                NavigationBar {
                    Tab.entries.forEach { tab ->
                        NavigationBarItem(
                            selected = route == tab.route,
                            onClick = { nav.switchTab(tab.route) },
                            icon = { Text(tab.icon) },
                            label = { Text(tab.label) },
                        )
                    }
                }
            }
        },
    ) { padding ->
        NavHost(nav, startDestination = Tab.Today.route, modifier = Modifier.padding(padding)) {
            composable(Tab.Today.route) {
                TodayScreen(
                    state = state,
                    onTeaBreak = { nav.switchTab(Tab.Practice.route) },
                    onStation = { nav.navigate("station/$it") },
                    onTestDay = { nav.switchTab(Tab.TestDay.route) },
                )
            }
            composable(Tab.Journey.route) {
                JourneyScreen(state, onStation = { nav.navigate("station/$it") })
            }
            composable("station/{id}") { entry ->
                StationScreen(
                    state = state,
                    stationId = entry.arguments?.getString("id").orEmpty(),
                    onPractice = { nav.navigate("practice/$it") },
                )
            }
            composable(Tab.Practice.route) {
                PracticeScreen(state, stationId = null)
            }
            composable("practice/{stationId}") { entry ->
                PracticeScreen(state, stationId = entry.arguments?.getString("stationId"))
            }
            composable(Tab.TestDay.route) {
                TestDayScreen(state, onStart = { mini -> nav.navigate(if (mini) "mock/mini" else "mock/full") })
            }
            composable("mock/{kind}") { entry ->
                MockExamScreen(
                    state = state,
                    mini = entry.arguments?.getString("kind") == "mini",
                    onExit = { nav.popBackStack() },
                )
            }
            composable(Tab.Passport.route) {
                PassportScreen(state)
            }
        }
    }
}

private fun NavHostController.switchTab(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
