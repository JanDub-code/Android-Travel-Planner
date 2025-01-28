package cz.pef.TravelPlanner.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import cz.pef.TravelPlanner.navigation.Destination
import cz.pef.TravelPlanner.navigation.NavGraph
import cz.pef.TravelPlanner.ui.theme.VA2_2024_PetstoreTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VA2_2024_PetstoreTheme {
                NavGraph(startDestination = Destination.SplashScreen.route)
            }
        }
    }
}