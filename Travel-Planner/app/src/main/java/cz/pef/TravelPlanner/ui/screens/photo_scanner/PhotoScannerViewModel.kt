package cz.pef.TravelPlanner.ui.screens.photo_scanner

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import cz.pef.TravelPlanner.database.ITravelRepository
import cz.pef.TravelPlanner.models.UserSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoScannerViewModel @Inject constructor(

) : ViewModel() {


    suspend fun analyzeImage(
        context: Context,
        photoUri: Uri,
        onResult: (List<String>) -> Unit
    ) {
        try {
            val inputImage = InputImage.fromFilePath(context, photoUri)
            val labeler: ImageLabeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

            labeler.process(inputImage)
                .addOnSuccessListener { labels ->
                    val detectedLabels = labels.map(ImageLabel::getText) // Extrahuje popisky
                        .take(10)
                    onResult(detectedLabels)
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    onResult(emptyList()) // Pokud selže, vrací prázdný seznam
                }
        } catch (e: Exception) {
            e.printStackTrace()
            onResult(emptyList())
        }
    }

}
