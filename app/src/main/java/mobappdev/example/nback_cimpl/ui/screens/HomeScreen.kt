package mobappdev.example.nback_cimpl.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mobappdev.example.nback_cimpl.ui.viewmodels.FakeVM
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel

@Composable
fun HomeScreen(
    vm: GameViewModel,
    onStartGame: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenRules: () -> Unit
) {
    val highscore by vm.highscore.collectAsState()
    val nBackValue by vm.nBackFlow.collectAsState()
    val stimulusType by vm.stimulusTypeFlow.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "High Score = $highscore",
                modifier = Modifier.padding(32.dp)
            )

            Text(text = "N-back: $nBackValue", modifier = Modifier.padding(8.dp))
            Text(text = "Stimulus-type: ${getStimulusTypeLabel(stimulusType)}", modifier = Modifier.padding(8.dp))

            Button(onClick = onStartGame, modifier = Modifier.padding(top = 16.dp)) {
                Text("Start Game")
            }

            Button(onClick = onOpenSettings, modifier = Modifier.padding(top = 16.dp)) {
                Text("Open Settings")
            }

            Button(onClick = onOpenRules, modifier = Modifier.padding(top = 16.dp)) {
                Text("Rules")
            }
        }
    }
}

fun getStimulusTypeLabel(stimulusType: Int): String {
    return when (stimulusType) {
        0 -> "Visual"
        1 -> "Audio"
        2 -> "DualBack"
        else -> "Unknown"
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    Surface {
        HomeScreen(
            vm = FakeVM(),
            onStartGame = {},
            onOpenSettings = {},
            onOpenRules = {}
        )
    }
}
