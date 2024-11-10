package mobappdev.example.nback_cimpl.ui.viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeVM : GameViewModel
{
    override val gameState: StateFlow<GameState> = MutableStateFlow(GameState())
    override val score: StateFlow<Int> = MutableStateFlow(0)
    override val highscore: StateFlow<Int> = MutableStateFlow(0)
    override val nBackFlow: StateFlow<Int> = MutableStateFlow(2)
    override val stimulusTypeFlow: StateFlow<Int> = MutableStateFlow(0)
    override val numberOfEvents: StateFlow<Int> = MutableStateFlow(10)
    override val eventInterval: StateFlow<Long> = MutableStateFlow(2000L)
    override val gridSize: StateFlow<Int> = MutableStateFlow(3)
    override val audioLetterCount: StateFlow<Int> = MutableStateFlow(3)
    override val isError: StateFlow<Boolean> = MutableStateFlow(false)

    override fun setGameType(gameType: GameType) {}

    override fun startGame() {}

    override fun checkMatch() {}

    override fun stopGame() {}

    override suspend fun setNBackValue(value: Int) {}

    override suspend fun setStimulusType(type: Int) {}

    override suspend fun setNumberOfEvents(count: Int) {}

    override suspend fun setEventInterval(interval: Long) {}

    override suspend fun setGridSize(size: Int) {}

    override suspend fun setAudioLetterCount(count: Int) {}
}
