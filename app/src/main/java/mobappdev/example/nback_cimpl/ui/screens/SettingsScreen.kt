package mobappdev.example.nback_cimpl.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel

@Composable
fun SettingsScreen(
    vm: GameViewModel,
    onBackToHome: () -> Unit
) {
    val nBackValue by vm.nBackFlow.collectAsState()
    val stimulusType by vm.stimulusTypeFlow.collectAsState()
    val numberOfEvents by vm.numberOfEvents.collectAsState()
    val eventInterval by vm.eventInterval.collectAsState()
    val gridSize by vm.gridSize.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineSmall)

        Text("N-back value: $nBackValue")
        Row {
            Button(onClick = { coroutineScope.launch { vm.setNBackValue(2) } }) {
                Text("2-back")
            }
            Button(onClick = { coroutineScope.launch { vm.setNBackValue(3) } }) {
                Text("3-back")
            }
        }

        Text("Stimulus: ${getStimulusTypeLabel(stimulusType)}")
        Row {
            Button(onClick = { coroutineScope.launch { vm.setStimulusType(0) } }) {
                Text("Visual")
            }
            Button(onClick = { coroutineScope.launch { vm.setStimulusType(1) } }) {
                Text("Audio")
            }
            Button(onClick = { coroutineScope.launch { vm.setStimulusType(2) } }) {
                Text("DualBackN")
            }
        }

        Text("NrOf events: $numberOfEvents")
        Row {
            Button(onClick = { coroutineScope.launch { vm.setNumberOfEvents(50) } }) {
                Text("50")
            }
            Button(onClick = { coroutineScope.launch { vm.setNumberOfEvents(100) } }) {
                Text("100")
            }
        }

        Text("Time interval (ms): $eventInterval ms")
        Row {
            Button(onClick = { coroutineScope.launch { vm.setEventInterval(1000L) } }) {
                Text("1000 ms")
            }
            Button(onClick = { coroutineScope.launch { vm.setEventInterval(2000L) } }) {
                Text("2000 ms")
            }
        }

        Text("GridSize: $gridSize x $gridSize")
        Row {
            Button(onClick = { coroutineScope.launch { vm.setGridSize(3) } }) {
                Text("3x3")
            }
            Button(onClick = { coroutineScope.launch { vm.setGridSize(5) } }) {
                Text("5x5")
            }
        }

        Button(onClick = onBackToHome, modifier = Modifier.padding(top = 24.dp)) {
            Text("Save & return")
        }
    }
}