package mobappdev.example.nback_cimpl.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel

@Composable
fun GameScreen(vm: GameViewModel, onExitGame: () -> Unit) {
    val gameState by vm.gameState.collectAsState()
    val score by vm.score.collectAsState()
    val isError by vm.isError.collectAsState()
    val stimulusType by vm.stimulusTypeFlow.collectAsState()
    val gridSize by vm.gridSize.collectAsState()

    LaunchedEffect(Unit) {
        vm.startGame()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Score: $score")

        if (stimulusType == 0 || stimulusType == 2) {
            if (gameState.eventValue != -1) {
                DynamicGrid(gridSize = gridSize, currentPosition = gameState.eventValue)
            }
        }

        Button(
            onClick = { vm.checkMatch() },
            modifier = Modifier
                .padding(8.dp)
                .background(if (isError) Color.Red else Color.Green)
        ) {
            Text("Match")
        }

        Button(
            onClick = {
                vm.stopGame()
                onExitGame()
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Stop Game")
        }
    }
}

@Composable
fun DynamicGrid(gridSize: Int, currentPosition: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        for (row in 0 until gridSize) {
            androidx.compose.foundation.layout.Row(
                horizontalArrangement = Arrangement.Center
            ) {
                for (col in 0 until gridSize) {
                    val index = row * gridSize + col
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .background(
                                if (index == currentPosition) Color.Cyan else Color.Black
                            )
                            .size(50.dp)
                    )
                }
            }
        }
    }
}
