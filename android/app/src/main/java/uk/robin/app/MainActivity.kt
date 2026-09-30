package uk.robin.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import uk.robin.app.ui.RobinApp
import uk.robin.app.ui.theme.RobinTheme

class MainActivity : ComponentActivity() {
    private val appState: AppState by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RobinTheme {
                RobinApp(appState)
            }
        }
    }
}
