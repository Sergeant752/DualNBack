package mobappdev.example.nback_cimpl.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val HIGHSCORE = intPreferencesKey("highscore")
        val NBACK_VALUE = intPreferencesKey("nback_value")
        val STIMULUS_TYPE = intPreferencesKey("stimulus_type")
        val NUMBER_OF_EVENTS = intPreferencesKey("number_of_events")
        val EVENT_INTERVAL = longPreferencesKey("event_interval")
        val GRID_SIZE = intPreferencesKey("grid_size")
        val AUDIO_LETTER_COUNT = intPreferencesKey("audio_letter_count")
        const val TAG = "UserPreferencesRepo"
    }

    val highscore: Flow<Int> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[HIGHSCORE] ?: 0
        }

    val nBackValue: Flow<Int> = dataStore.data
        .map { preferences -> preferences[NBACK_VALUE] ?: 2 }

    val stimulusType: Flow<Int> = dataStore.data
        .map { preferences -> preferences[STIMULUS_TYPE] ?: 0 }

    val numberOfEvents: Flow<Int> = dataStore.data
        .map { preferences -> preferences[NUMBER_OF_EVENTS] ?: 10 }

    val eventInterval: Flow<Long> = dataStore.data
        .map { preferences -> preferences[EVENT_INTERVAL] ?: 2000L }

    val gridSize: Flow<Int> = dataStore.data
        .map { preferences -> preferences[GRID_SIZE] ?: 3 }

    val audioLetterCount: Flow<Int> = dataStore.data
        .map { preferences -> preferences[AUDIO_LETTER_COUNT] ?: 3 }

    suspend fun saveHighScore(score: Int) {
        dataStore.edit { preferences -> preferences[HIGHSCORE] = score }
    }

    suspend fun saveNBackValue(value: Int) {
        dataStore.edit { preferences -> preferences[NBACK_VALUE] = value }
    }

    suspend fun saveStimulusType(type: Int) {
        dataStore.edit { preferences -> preferences[STIMULUS_TYPE] = type }
    }

    suspend fun saveNumberOfEvents(value: Int) {
        dataStore.edit { preferences -> preferences[NUMBER_OF_EVENTS] = value }
    }

    suspend fun saveEventInterval(value: Long) {
        dataStore.edit { preferences -> preferences[EVENT_INTERVAL] = value }
    }

    suspend fun saveGridSize(value: Int) {
        dataStore.edit { preferences -> preferences[GRID_SIZE] = value }
    }

    suspend fun saveAudioLetterCount(value: Int) {
        dataStore.edit { preferences -> preferences[AUDIO_LETTER_COUNT] = value }
    }
}
