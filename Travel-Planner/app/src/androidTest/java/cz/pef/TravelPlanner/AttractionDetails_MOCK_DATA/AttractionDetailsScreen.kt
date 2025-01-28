package cz.pef.TravelPlanner.AttractionDetails_MOCK_DATA

import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun MockAttractionDetailsScreen(
    viewModel: MockAttractionDetailsViewModel,
    onConfirmClick: (String) -> Unit
) {
    val place by viewModel.place.collectAsState()
    val selectedOption by viewModel.customTravelStyle.collectAsState()

    if (place != null) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(text = place!!.name, style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            // Možnosti výběru
            val options = listOf("DRIVING", "WALKING", "TRANSIT")
            options.forEach { option ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = (option == selectedOption),
                        onClick = { viewModel.setTravelStyle(option) },
                        modifier = Modifier.testTag("RadioButton_$option")
                    )
                    Text(
                        text = option,
                        modifier = Modifier.padding(start = 8.dp).testTag("RadioText_$option")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Potvrzovací tlačítko
            Button(
                onClick = { onConfirmClick(selectedOption) },
                modifier = Modifier.fillMaxWidth().testTag("ConfirmButton")
            ) {
                Text(text = "Confirm")
            }
        }
    }
}
