package cz.pef.TravelPlanner.ui.screens.select_user_location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import cz.pef.TravelPlanner.database.ITravelRepository
import cz.pef.TravelPlanner.models.UserSettings
import cz.pef.TravelPlanner.ui.screens.settings_proflle.SettingsProfileData
import cz.pef.TravelPlanner.ui.screens.settings_proflle.SettingsProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectLocationViewModel @Inject constructor(
    private val repository: ITravelRepository
) : ViewModel() {
    private val _selectLocationUIState: MutableStateFlow<SelectLocationUIState> =
        MutableStateFlow(SelectLocationUIState.Loading())
    val selectLocationUIState = _selectLocationUIState.asStateFlow()
    private val data: SelectLocationData = SelectLocationData()
    var userSettings: UserSettings? = null
        private set

    init {
        viewModelScope.launch {
            userSettings = repository.getUserSettings()
            if (userSettings == null) {
                userSettings = UserSettings()
                repository.insertOrUpdateUserSettings(userSettings!!)
            }
            data.userSettings = userSettings!!
            // Update the UI state to notify the Composable
            _selectLocationUIState.value = SelectLocationUIState.ScreenDataChanged(data)
        }
    }

    fun saveSelectedLocation(lat: Double, lng: Double) {
        viewModelScope.launch {
            userSettings?.let {
                it.latitude = lat
                it.longitude = lng
                repository.insertOrUpdateUserSettings(it)
                data.userSettings = it
                // Update the UI state to notify the Composable
                _selectLocationUIState.value = SelectLocationUIState.ScreenDataChanged(data)
            }
        }
    }
}
