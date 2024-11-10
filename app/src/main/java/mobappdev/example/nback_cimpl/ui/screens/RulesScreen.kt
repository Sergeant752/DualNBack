package mobappdev.example.nback_cimpl.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RulesScreen(
    onBackToHome: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Rules of the N-Back game",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "The N-back game tests your working memory by presenting a sequence of stimuli. " +
                        "Your task is to indicate when the current stimulus matches the one shown N steps earlier.\n\n" +
                        "1. * Visual Stimulus: A colored square appears in a grid (3x3 or 5x5). Identify when a square appears in the same position as N steps before.\n\n" +
                        "2. * Auditory Stimulus: A letter is spoken. Indicate when the current letter matches the one from N steps earlier.\n\n" +
                        "3. * Dual-Back (Combined Visual and Auditory): Both a square in the grid and a spoken letter are presented simultaneously. Track both stimuli and identify if either matches the one shown or spoken N steps before.\n\n" +
                        "Good luck!",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = onBackToHome,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Back to Home")
            }
        }
    }
}