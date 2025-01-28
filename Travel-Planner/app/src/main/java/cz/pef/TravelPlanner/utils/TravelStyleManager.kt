package cz.pef.TravelPlanner.utils

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TravelStyleManager @Inject constructor(@ApplicationContext context: Context) {

    val dataStore = context.dataStore

    companion object {
        private val Context.dataStore: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore(name = "travel_preference")
        val TRAVEL_STRING = stringPreferencesKey("custom_marker_enabled")
    }

    val customTravelStyleFlow: Flow<String> = dataStore.data
        .map { preferences ->
            val value = preferences[TRAVEL_STRING] ?: "failed_to_save"
            // Log pro kontrolu načtení
            Log.d("PreferencesManager", "Custom travel Loaded: $value")
            value
        }


    suspend fun setCustomTravelStyle(enabled: String) {
        dataStore.edit { preferences ->
            preferences[TRAVEL_STRING] = enabled
            // Log pro kontrolu uložení
            Log.d("PreferencesManager", "Custom travel Saved: $enabled")
        }
    }


}