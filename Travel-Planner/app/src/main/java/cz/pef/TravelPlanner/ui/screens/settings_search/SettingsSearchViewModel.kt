package cz.pef.TravelPlanner.ui.screens.settings_search

import android.util.Log
import cz.pef.TravelPlanner.ui.screens.settings_proflle.SettingsProfileData
import cz.pef.TravelPlanner.ui.screens.settings_proflle.SettingsProfileUiState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pef.TravelPlanner.database.ITravelRepository
import cz.pef.TravelPlanner.models.UserSettings
import cz.pef.TravelPlanner.utils.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsSearchViewModel @Inject constructor(
    private val repository: ITravelRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _settingsSearchUIState: MutableStateFlow<SettingsSearchUIState> = MutableStateFlow(
        SettingsSearchUIState.Loading())
    private val data : SettingsSearchData = SettingsSearchData()
    val settingsSearchUIState = _settingsSearchUIState.asStateFlow()

    private val _userSettings = MutableLiveData<UserSettings?>()
    val userSettings: MutableLiveData<UserSettings?> get() = _userSettings

   private val _customMarkerEnabled = MutableStateFlow(false)
    val customMarkerEnabled = _customMarkerEnabled.asStateFlow()
   private val dataStore = preferencesManager.dataStore

    init {
        viewModelScope.launch {
            val settings = repository.getUserSettings()
            if (settings != null) {
                data.userSettings = settings
            } else {
                data.userSettings = UserSettings(
                    id = null,
                    username = "",
                    email = "",
                    maxAttractionsDisplayed = 10,
                    maxLocationDistance = 10
                )
                repository.insertOrUpdateUserSettings(data.userSettings)
            }

            preferencesManager.customMarkerEnabledFlow.collectLatest { enabled ->
                _customMarkerEnabled.value = enabled
            }
        }
    }

    fun loadSettings() {
        viewModelScope.launch {
            _settingsSearchUIState.update {
                SettingsSearchUIState.ScreenDataChanged(data)
            }
        }
    }
    fun attractionChanged(text: String) {
        val maxAttractions = text.toIntOrNull()
        if (maxAttractions != null) {
            data.userSettings.maxAttractionsDisplayed = maxAttractions
            _settingsSearchUIState.update {
                SettingsSearchUIState.ScreenDataChanged(data)
            }
        } else {
        }
    }


    fun distanceChanged(text: String) {
        val maxDistance = text.toIntOrNull()
        if (maxDistance != null) {
            data.userSettings.maxLocationDistance = maxDistance
            _settingsSearchUIState.update {
                SettingsSearchUIState.ScreenDataChanged(data)
            }
        } else {
        }
    }

    fun saveSettings(){
        viewModelScope.launch {
            repository.insertOrUpdateUserSettings(data.userSettings)
        }
    }

    fun toggleCustomMarker(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setCustomMarkerEnabled(enabled)
        }
    }


}
