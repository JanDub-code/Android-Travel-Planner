package cz.pef.TravelPlanner.models
import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.pef.TravelPlanner.communication.Geometry
import cz.pef.TravelPlanner.communication.Photo

@Entity(tableName = "place_result")
data class PlaceResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val vicinity: String?,
    val rating: Double?,
    val lat: Double,
    val lng: Double,
    val photo_reference: String?,
    val icon: String?
)

