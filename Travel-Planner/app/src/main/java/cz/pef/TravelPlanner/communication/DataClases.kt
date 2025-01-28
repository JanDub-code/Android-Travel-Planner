package cz.pef.TravelPlanner.communication

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import java.io.Serializable

data class PlaceResponse(
    val results: List<PlaceResult>
)

data class PlaceResult(
    val name: String,
    val vicinity: String?,
    val rating: Double?,
    val geometry: Geometry,
    val photos: List<Photo>?,
    val icon: String?
): Serializable, ClusterItem {

    override fun getPosition(): LatLng {
        return LatLng(geometry.location.lat!!, geometry.location.lng!!)
    }

    override fun getTitle(): String {
        return rating.toString()
    }

    override fun getSnippet(): String {
        return rating.toString()
    }

    override fun getZIndex(): Float {
        return 0.0f
    }
}

data class Geometry(
    val location: Location
)

data class Location(
    val lat: Double,
    val lng: Double
)

data class Photo(
    val photo_reference: String
)

data class DirectionResponse(
    val routes: List<Route>,
    val status: String
)

data class Route(
    val legs: List<Leg>,
    val overview_polyline: Polyline,
    val summary: String,
    val warnings: List<String>
)

data class Leg(
    val steps: List<Step>,
    val distance: TextValue,
    val duration: TextValue,
    val start_address: String,
    val end_address: String,
    val start_location: Location,
    val end_location: Location
)

data class Step(
    val distance: TextValue,
    val duration: TextValue,
    val start_location: Location,
    val end_location: Location,
    val html_instructions: String,
    val polyline: Polyline,
    val travel_mode: String
)

data class Polyline(
    val points: String
)

data class TextValue(
    val text: String,
    val value: Int
)


