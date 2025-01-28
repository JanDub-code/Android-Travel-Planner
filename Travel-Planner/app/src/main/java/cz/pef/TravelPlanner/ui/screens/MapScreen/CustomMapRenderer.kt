package cz.pef.TravelPlanner.ui.screens.MapScreen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import cz.pef.TravelPlanner.R
import cz.pef.TravelPlanner.communication.PlaceResult


class MapRenderer (
    val context: Context,
    val googleMap: GoogleMap,
    val clusterManager: ClusterManager<PlaceResult>,
    private val isCustomMarkerEnabled: Boolean
): DefaultClusterRenderer<PlaceResult>(context, googleMap, clusterManager) {

    override fun shouldRenderAsCluster(cluster: Cluster<PlaceResult>): Boolean {
        return cluster.size > 1
    }

    fun resizeBitmap(resourceId: Int, context: android.content.Context, width: Int, height: Int): BitmapDescriptor {
        val bitmap = BitmapFactory.decodeResource(context.resources, resourceId)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
        return BitmapDescriptorFactory.fromBitmap(resizedBitmap)
    }

    fun getIcon(): BitmapDescriptor {
        return resizeBitmap(R.drawable.pin_purple,context,150,150)
    }

    /*fun getIconForType(type: String, context: android.content.Context): BitmapDescriptor {
        return when (type) {
            "basic" -> resizeBitmap(R.drawable.e, context, 170, 170)
            "middle" -> resizeBitmap(R.drawable.middle, context, 170, 170)
            "university" -> resizeBitmap(R.drawable.university, context, 170, 170)
            else -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
        }
    }*/


    override fun onBeforeClusterItemRendered(item: PlaceResult, markerOptions: MarkerOptions) {
        //markerOptions.icon(getIconForType(item.type,context))
        if(isCustomMarkerEnabled){
            markerOptions.icon(getIcon())
        }else{

        }
    }


}