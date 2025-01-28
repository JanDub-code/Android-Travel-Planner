package cz.pef.TravelPlanner.ui.screens.settings_interests
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pef.TravelPlanner.database.ITravelRepository
import cz.pef.TravelPlanner.models.UserSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterestsViewModel @Inject constructor(
    private val repository: ITravelRepository
) : ViewModel() {

    private val _selectedInterests = MutableStateFlow<List<String>>(emptyList())
    val selectedInterests = _selectedInterests.asStateFlow()
    init {
        loadInterests()
    }

    fun toggleInterest(name: String) {
        viewModelScope.launch {
            if (_selectedInterests.value.contains(name)) {
                repository.deleteInterest(name)
            } else {
                repository.addInterest(name)
            }
            loadInterests()
        }
    }

    private fun loadInterests() {
        viewModelScope.launch {
            _selectedInterests.value = repository.getAllInterests()
        }
    }

    fun addInterestsFromPhoto() {

    }

}
