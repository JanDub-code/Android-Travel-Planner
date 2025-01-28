package cz.pef.TravelPlanner.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettings(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var username: String = "",
    var email: String = "",
    var maxAttractionsDisplayed: Int? = 10,
    var maxLocationDistance: Int? = 10,
    var latitude: Double? = 49.1951,
    var longitude: Double? = 16.6068
)