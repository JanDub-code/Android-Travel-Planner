package cz.pef.TravelPlanner.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_places")
data class SavedPlaceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val vicinity: String?,
    val rating: Double?,
    val lat: Double,
    val lng: Double,
    val photo_reference: String?,
    val icon: String?,
    val notes: String? // Uživatelské poznámky (volitelné)
)
