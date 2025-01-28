package cz.pef.TravelPlanner.utils
import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.core.DataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map


@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    val dataStore = context.dataStore

    companion object {
        private val Context.dataStore: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
        val CUSTOM_MARKER_ENABLED_KEY = booleanPreferencesKey("custom_marker_enabled")
    }

    // Flow pro sledování změn preference
    val customMarkerEnabledFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            val value = preferences[CUSTOM_MARKER_ENABLED_KEY] ?: false
            // Log pro kontrolu načtení
            Log.d("PreferencesManager", "Custom Marker Loaded: $value")
            value
        }


    suspend fun setCustomMarkerEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[CUSTOM_MARKER_ENABLED_KEY] = enabled
            // Log pro kontrolu uložení
            Log.d("PreferencesManager", "Custom Marker Saved: $enabled")
        }
    }


}
