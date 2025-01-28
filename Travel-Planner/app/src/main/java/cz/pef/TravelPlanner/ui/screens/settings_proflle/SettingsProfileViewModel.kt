package cz.pef.TravelPlanner.ui.screens.settings_proflle

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pef.TravelPlanner.BuildConfig
import cz.pef.TravelPlanner.database.ITravelRepository
import cz.pef.TravelPlanner.models.UserSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject

@HiltViewModel
class SettingsProfileViewModel @Inject constructor(
    private val repository: ITravelRepository
) : ViewModel() {

    private val _settingsProfileUIState: MutableStateFlow<SettingsProfileUiState> = MutableStateFlow(SettingsProfileUiState.Loading())
    private val data : SettingsProfileData = SettingsProfileData()
    val settingsProfileUIState = _settingsProfileUIState.asStateFlow()

    private val _userSettings = MutableLiveData<UserSettings?>()
    val userSettings: MutableLiveData<UserSettings?> get() = _userSettings

    val apikey = BuildConfig.API_KEY

    var intrerests = listOf("Adventure","Culture","Leisure","Food","Park","Restaurant")

    init {
        //val apiKey ="AIzaSyC0OTiwAcTtmI7rCH90N2m0IhR7Lk5RIRA"
        //fetchPlacesNearbyBrno(apikey)
        //fetchPlaces(apiKey)
        //fetchWalkingTime(apikey)
        //fetchBalancedPlacesByInterests(apikey,intrerests)

        viewModelScope.launch {
            val settings = repository.getUserSettings()
            if (settings != null) {
                data.userSettings = settings
            } else {
                // Nastavíme výchozí hodnoty, pokud není záznam v databázi
                data.userSettings = UserSettings(
                    id = null,
                    username = "",
                    email = "",
                    maxAttractionsDisplayed = 10, // Defaultní hodnota
                    maxLocationDistance = 10// Defaultní hodnota
                )
                // Uložíme výchozí hodnoty do databáze
                repository.insertOrUpdateUserSettings(data.userSettings)
            }
        }
    }

    fun loadSettings() {
        viewModelScope.launch {
            _settingsProfileUIState.update {
                SettingsProfileUiState.ScreenDataChanged(data)
            }
        }
    }

    fun usernameChanged(text : String) {
        var username = text

        data.userSettings.username = username
        _settingsProfileUIState.update {
            SettingsProfileUiState.ScreenDataChanged(data)
        }
    }

    fun emailChanged(text : String) {
        var email = text

        data.userSettings.email = email
        _settingsProfileUIState.update {
            SettingsProfileUiState.ScreenDataChanged(data)
        }
    }

    fun saveSettings(){
        viewModelScope.launch {
            repository.insertOrUpdateUserSettings(data.userSettings)
        }
    }
/*
    fun fetchPlaces(apiKey: String) {
        // Spustíme síťovou operaci v IO threadu
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient()
                // Příklad API dotazu: hledáme restaurace v New Yorku
                //val url = "https://maps.googleapis.com/maps/api/place/textsearch/json?" +
                  //      "query=restaurants+in+New+York&key=$apiKey"
                val url = "https://maps.googleapis.com/maps/api/place/textsearch/json?" +
                        "query=restaurants+in+New+York&key=$key"

                val request = Request.Builder()
                    .url(url)
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (response.isSuccessful && responseBody != null) {
                    println("API Response: $responseBody") // Výstup do konzole
                } else {
                    println("Error: ${response.message}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }*/




    var isCallCompleted = false // Globální proměnná pro kontrolu

    fun fetchPlacesNearbyBrno(apiKey: String) {
        if (isCallCompleted) return // Neprovádějte znovu

        val latitude = 49.1951 // Souřadnice Brna
        val longitude = 16.6068
        val radius = 5000 // Poloměr 5 km

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient()
                val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                        "location=$latitude,$longitude&radius=$radius&type=restaurant&key=$apiKey"

                val request = Request.Builder()
                    .url(url)
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (response.isSuccessful && responseBody != null) {
                    // Parse JSON response
                    val jsonResponse = JSONObject(responseBody)
                    val results = jsonResponse.getJSONArray("results")
                    val limitedResults = results.take(10) // Limit na prvních 10 míst

                    // Výpis míst
                    limitedResults.forEach { result ->
                        val name = result.optString("name")
                        val address = result.optString("vicinity")
                        val rating = result.optDouble("rating", -1.0)
                        val geometry = result.optJSONObject("geometry")
                        val location = geometry?.optJSONObject("location")
                        val lat = location?.optDouble("lat", 0.0)
                        val lng = location?.optDouble("lng", 0.0)
                        val photos = result.optJSONArray("photos")
                        val photoReference = photos?.optJSONObject(0)?.optString("photo_reference")

                        println("Name: $name")
                        println("Address: $address")
                        println("Rating: $rating")
                        println("Latitude: $lat, Longitude: $lng")
                        println("Photo Reference: $photoReference")
                        println("------------")
                    }
                } else {
                    println("Error: ${response.message}")
                }

                isCallCompleted = true // Nastavíme příznak, že volání bylo dokončeno
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Rozšíření pro získání prvních X položek z JSONArray
    fun JSONArray.take(limit: Int): List<JSONObject> {
        return (0 until minOf(length(), limit)).map { index ->
            getJSONObject(index)
        }
    }

    fun fetchWalkingTime(apiKey: String) {
        val origin = "49.1951,16.6068"
        val destination = "49.1946198,16.5644835"
        val mode = "walking"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient()
                val url = "https://maps.googleapis.com/maps/api/directions/json?" +
                        "origin=$origin&destination=$destination&mode=$mode&key=$apiKey"

                val request = Request.Builder()
                    .url(url)
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (response.isSuccessful && responseBody != null) {
                    val jsonResponse = JSONObject(responseBody)
                    val routes = jsonResponse.getJSONArray("routes")
                    if (routes.length() > 0) {
                        val legs = routes.getJSONObject(0).getJSONArray("legs")
                        if (legs.length() > 0) {
                            val duration = legs.getJSONObject(0).getJSONObject("duration")
                            val distance = legs.getJSONObject(0).getJSONObject("distance")
                            val durationText = duration.getString("text")
                            val distanceText = distance.getString("text")
                            println("Doba chůze: $durationText")
                            println("Vzdálenost: $distanceText")
                        } else {
                            println("Nebyla nalezena žádná trasa.")
                        }
                    } else {
                        println("Nebyla nalezena žádná trasa.")
                    }
                } else {
                    println("Chyba: ${response.message}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun fetchBalancedPlacesByInterests(apiKey: String, interests: List<String>) {
        val latitude = 49.1951 // Souřadnice Brna
        val longitude = 16.6068
        val radius = 5000 // Poloměr 5 km

        // Počet iterací pro každý zájem (zaokrouhlení nahoru)
        val iterationsPerInterest = (10.0 / interests.size).toInt()
        var totalRequests = 0 // Počítadlo všech požadavků

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient()

                interests.forEach { interest ->
                    var iteration = 0
                    while (iteration < iterationsPerInterest+1 && totalRequests < 10+1) {
                        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                                "location=$latitude,$longitude&radius=$radius&keyword=$interest&key=$apiKey"

                        val request = Request.Builder()
                            .url(url)
                            .build()

                        val response = client.newCall(request).execute()
                        val responseBody = response.body?.string()

                        if (response.isSuccessful && responseBody != null) {
                            val jsonResponse = JSONObject(responseBody)
                            val results = jsonResponse.getJSONArray("results")

                            if (results.length() > iteration) {
                                // Vezměte pouze jednu lokaci na iteraci
                                val result = results.getJSONObject(iteration)
                                val name = result.optString("name")
                                val address = result.optString("vicinity")
                                val rating = result.optDouble("rating", -1.0)
                                val geometry = result.optJSONObject("geometry")
                                val location = geometry?.optJSONObject("location")
                                val lat = location?.optDouble("lat", 0.0)
                                val lng = location?.optDouble("lng", 0.0)
                                val photos = result.optJSONArray("photos")
                                val photoReference = photos?.optJSONObject(0)?.optString("photo_reference")

                                println("Interest: $interest (Iteration: ${iteration + 1})")
                                println("Name: $name")
                                println("Address: $address")
                                println("Rating: $rating")
                                println("Latitude: $lat, Longitude: $lng")
                                println("Photo Reference: $photoReference")
                                println("------------")
                            } else {
                                println("No more results for interest: $interest (Iteration: ${iteration + 1})")
                            }
                        } else {
                            println("Error for interest $interest: ${response.message}")
                        }

                        iteration++
                        totalRequests++
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }





}
