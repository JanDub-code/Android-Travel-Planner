package cz.pef.TravelPlanner.ui.screens.SplashScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import androidx.navigation.NavController
import cz.pef.TravelPlanner.navigation.Destination
import cz.pef.TravelPlanner.navigation.INavigationRouter
import cz.pef.TravelPlanner.ui.screens.settings_interests.InterestsViewModel

@Composable
fun SplashScreen(navigation: INavigationRouter,) {
    val viewModel = hiltViewModel<InterestsViewModel>()
    val selectedInterests by viewModel.selectedInterests.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        delay(1000)
        if(selectedInterests.isEmpty()){
            navigation.navitageToInterests()
        }else{
            navigation.navigateToMap()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2563EB)), // Modrá barva pozadí
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Travel Planner",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Explore the World with Ease",
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}