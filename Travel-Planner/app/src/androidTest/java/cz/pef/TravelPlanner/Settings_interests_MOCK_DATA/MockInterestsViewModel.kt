package cz.pef.TravelPlanner.Settings_interests_MOCK_DATA
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pef.TravelPlanner.database.ITravelRepository
import cz.pef.TravelPlanner.models.UserSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MockInterestsViewModel(
    private val repository: ITravelRepository
) : ViewModel() {

    private val _selectedInterests = MutableStateFlow<List<String>>(emptyList())
    val selectedInterests = _selectedInterests.asStateFlow()

    // Flag pro ověření saveSettings
    var saveCalled: Boolean = false

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
        // Implementace přidání zájmů z fotky, pokud je potřeba
    }

    // Metoda pro testování
    fun isInterestSelected(name: String): Boolean {
        return _selectedInterests.value.contains(name)
    }

    fun saveSettings() {
        saveCalled = true
        // Implementace uložení zájmů, např. volání repository
    }
}
