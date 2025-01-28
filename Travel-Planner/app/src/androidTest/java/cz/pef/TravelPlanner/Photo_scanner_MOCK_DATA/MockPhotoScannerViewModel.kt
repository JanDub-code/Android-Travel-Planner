package cz.pef.TravelPlanner.Photo_scanner_MOCK_DATA

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import cz.pef.TravelPlanner.ui.screens.photo_scanner.PhotoScannerViewModel
import kotlinx.coroutines.launch

open class MockPhotoScannerViewModel : ViewModel() {


    open suspend fun analyzeImage(
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
