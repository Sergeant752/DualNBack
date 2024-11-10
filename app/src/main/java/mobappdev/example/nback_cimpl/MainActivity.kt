package mobappdev.example.nback_cimpl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import mobappdev.example.nback_cimpl.ui.screens.GameScreen
import mobappdev.example.nback_cimpl.ui.screens.HomeScreen
import mobappdev.example.nback_cimpl.ui.screens.RulesScreen
import mobappdev.example.nback_cimpl.ui.screens.SettingsScreen
import mobappdev.example.nback_cimpl.ui.theme.NBack_CImplTheme
import mobappdev.example.nback_cimpl.ui.viewmodels.GameVM

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NBack_CImplTheme {
                val gameViewModel: GameVM = viewModel(factory = GameVM.Factory)

                // Håll koll på aktuell skärm
                val currentScreen = remember { mutableStateOf("Home") }

                when (currentScreen.value) {
                    "Home" -> HomeScreen(
                        vm = gameViewModel,
                        onStartGame = { currentScreen.value = "Game" },
                        onOpenSettings = { currentScreen.value = "Settings" },
                        onOpenRules = { currentScreen.value = "Rules" }  // Hantera navigation till RulesScreen
                    )
                    "Game" -> GameScreen(
                        vm = gameViewModel,
                        onExitGame = { currentScreen.value = "Home" }
                    )
                    "Settings" -> SettingsScreen(
                        vm = gameViewModel,
                        onBackToHome = { currentScreen.value = "Home" }
                    )
                    "Rules" -> RulesScreen(
                        onBackToHome = { currentScreen.value = "Home" }  // Navigera tillbaka till HomeScreen
                    )
                }
            }
        }
    }
}
