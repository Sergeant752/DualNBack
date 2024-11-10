package mobappdev.example.nback_cimpl.ui.viewmodels

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mobappdev.example.nback_cimpl.GameApplication
import mobappdev.example.nback_cimpl.NBackHelper
import mobappdev.example.nback_cimpl.data.UserPreferencesRepository
import mobappdev.example.nback_cimpl.R

interface GameViewModel {
    val gameState: StateFlow<GameState>
    val score: StateFlow<Int>
    val highscore: StateFlow<Int>
    val nBackFlow: StateFlow<Int>
    val stimulusTypeFlow: StateFlow<Int>
    val numberOfEvents: StateFlow<Int>
    val eventInterval: StateFlow<Long>
    val gridSize: StateFlow<Int>
    val audioLetterCount: StateFlow<Int>
    val isError: StateFlow<Boolean>

    fun setGameType(gameType: GameType)
    fun startGame()
    fun checkMatch()
    fun stopGame()

    suspend fun setNBackValue(value: Int)
    suspend fun setStimulusType(type: Int)
    suspend fun setNumberOfEvents(count: Int)
    suspend fun setEventInterval(interval: Long)
    suspend fun setGridSize(size: Int)
    suspend fun setAudioLetterCount(count: Int)
}

class GameVM(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val context: Context
) : GameViewModel, ViewModel() {

    private val _gameState = MutableStateFlow(GameState())
    override val gameState: StateFlow<GameState> get() = _gameState.asStateFlow()

    private val _score = MutableStateFlow(0)
    override val score: StateFlow<Int> get() = _score.asStateFlow()

    private val _highscore = MutableStateFlow(0)
    override val highscore: StateFlow<Int> get() = _highscore.asStateFlow()

    private val _isError = MutableStateFlow(false)
    override val isError: StateFlow<Boolean> get() = _isError.asStateFlow()

    private val _nBack = MutableStateFlow(2)
    override val nBackFlow: StateFlow<Int> get() = _nBack.asStateFlow()

    private val _stimulusType = MutableStateFlow(0)
    override val stimulusTypeFlow: StateFlow<Int> get() = _stimulusType.asStateFlow()

    private val _numberOfEvents = MutableStateFlow(100)
    override val numberOfEvents: StateFlow<Int> get() = _numberOfEvents.asStateFlow()

    private val _eventInterval = MutableStateFlow(2000L)
    override val eventInterval: StateFlow<Long> get() = _eventInterval.asStateFlow()

    private val _gridSize = MutableStateFlow(3)
    override val gridSize: StateFlow<Int> get() = _gridSize.asStateFlow()

    private val _audioLetterCount = MutableStateFlow(3)
    override val audioLetterCount: StateFlow<Int> get() = _audioLetterCount.asStateFlow()


    private val nBackHelper = NBackHelper()
    private var events = emptyArray<Int>()
    private var currentIndex = 0
    private var job: Job? = null
    private var mediaPlayer: MediaPlayer? = null

    init {
        viewModelScope.launch {
            userPreferencesRepository.highscore.collect { _highscore.value = it }
            userPreferencesRepository.nBackValue.collect { _nBack.value = it }
            userPreferencesRepository.stimulusType.collect { _stimulusType.value = it }
            userPreferencesRepository.numberOfEvents.collect { _numberOfEvents.value = it }
            userPreferencesRepository.eventInterval.collect { _eventInterval.value = it }
            userPreferencesRepository.gridSize.collect { _gridSize.value = it }
            userPreferencesRepository.audioLetterCount.collect { _audioLetterCount.value = it }
        }
    }

    override fun checkMatch() {
        if (currentIndex >= _nBack.value) {
            val expectedValue = events[currentIndex - _nBack.value]
            val currentValue = events[currentIndex]
            if (currentValue == expectedValue) {
                _score.value += 1
                _isError.value = false
                Log.d("GameVM", "Match! Points: ${_score.value}")
            } else {
                _isError.value = true
                Log.d("GameVM", "No match!")
            }
        } else {
            _isError.value = false
        }
    }

    override fun setGameType(gameType: GameType) {
        _gameState.value = _gameState.value.copy(gameType = gameType)
    }

    override fun stopGame() {
        job?.cancel()
        mediaPlayer?.release()
        mediaPlayer = null
        updateHighScore()
    }

    override fun startGame() {
        job?.cancel()
        val nBack = _nBack.value
        val type = _stimulusType.value
        val numberOfEvents = _numberOfEvents.value
        val interval = _eventInterval.value

        events = nBackHelper.generateNBackString(numberOfEvents, _gridSize.value, 30, nBack).toList().toTypedArray()

        job = viewModelScope.launch {
            when (type) {
                0 -> runVisualGame(events, interval)
                1 -> runAudioGame(events, interval)
                2 -> runAudioVisualGame(events, interval)
            }
            updateHighScore()
        }
    }

    override suspend fun setNBackValue(value: Int) {
        _nBack.value = value
        userPreferencesRepository.saveNBackValue(value)
    }

    override suspend fun setStimulusType(type: Int) {
        _stimulusType.value = type
        userPreferencesRepository.saveStimulusType(type)
    }

    private fun updateHighScore() {
        if (_score.value > _highscore.value) {
            _highscore.value = _score.value
            viewModelScope.launch {
                userPreferencesRepository.saveHighScore(_highscore.value)
            }
        }
    }

    private suspend fun runVisualGame(events: Array<Int>, interval: Long) {
        for (i in events.indices) {
            currentIndex = i
            _gameState.value = _gameState.value.copy(eventValue = events[i])
            delay(interval)
        }
    }

    private suspend fun runAudioGame(events: Array<Int>, interval: Long) {
        for (i in events.indices) {
            currentIndex = i
            val eventValue = events[i]
            playAudio(eventValue)
            delay(interval)
        }
    }

    private suspend fun runAudioVisualGame(events: Array<Int>, interval: Long) {
        for (i in events.indices) {
            currentIndex = i
            val eventValue = events[i]
            _gameState.value = _gameState.value.copy(eventValue = eventValue)
            playAudio(eventValue)
            delay(interval)
        }
    }

    override suspend fun setNumberOfEvents(count: Int) {
        _numberOfEvents.value = count
        userPreferencesRepository.saveNumberOfEvents(count)
    }

    override suspend fun setEventInterval(interval: Long) {
        _eventInterval.value = interval
        userPreferencesRepository.saveEventInterval(interval)
    }

    override suspend fun setGridSize(size: Int) {
        _gridSize.value = size
        userPreferencesRepository.saveGridSize(size)
    }

    override suspend fun setAudioLetterCount(count: Int) {
        _audioLetterCount.value = count
        userPreferencesRepository.saveAudioLetterCount(count)
    }

    private fun playAudio(eventValue: Int) {
        mediaPlayer?.release()
        val soundResId = when (eventValue % _audioLetterCount.value) {
            0 -> R.raw.a
            1 -> R.raw.c
            2 -> R.raw.o
            else -> R.raw.y
        }
        mediaPlayer = MediaPlayer.create(context, soundResId)
        mediaPlayer?.start()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as GameApplication)
                GameVM(application.userPreferencesRespository, application.applicationContext)
            }
        }
    }
}

enum class GameType {
    Audio,
    Visual,
    AudioVisual
}

data class GameState(
    val gameType: GameType = GameType.Visual,
    val eventValue: Int = -1
)
