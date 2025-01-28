package cz.pef.TravelPlanner.ui.elements
import android.graphics.drawable.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cz.pef.TravelPlanner.R
import cz.pef.TravelPlanner.navigation.INavigationRouter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnNavBar(
    navigationRouter: INavigationRouter,
    selectedIndex: Int
) {
    val items = listOf(
        NavBarItem(
            title = stringResource(id = R.string.map),
            selectedIcon = painterResource(id = R.drawable.map),
            unselectedIcon = painterResource(id = R.drawable.map),
            navigateTo = { navigationRouter.navigateToMap() }
        ),
        NavBarItem(
            title = stringResource(id = R.string.itinerary),
            selectedIcon = painterResource(id = R.drawable.itinerary),
            unselectedIcon = painterResource(id = R.drawable.itinerary),
            navigateTo = { navigationRouter.navigateToItinerary() }
        ),
        NavBarItem(
            title = stringResource(id = R.string.attractions),
            selectedIcon = painterResource(id = R.drawable.attractions),
            unselectedIcon = painterResource(id = R.drawable.attractions),
            navigateTo = { navigationRouter.navigateToAttractions() }
        ),
        NavBarItem(
            title = stringResource(id = R.string.settings),
            selectedIcon = painterResource(id = R.drawable.settings),
            unselectedIcon = painterResource(id = R.drawable.settings),
            navigateTo = { navigationRouter.navigateToSettings() }
        )
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = {
                    item.navigateTo()
                },
                label = {
                    Text(text = item.title)
                },
                icon = {
                    Icon(
                        painter = if (selectedIndex == index) item.selectedIcon else item.unselectedIcon,
                        contentDescription = null
                    )
                }
            )
        }
    }
}

data class NavBarItem(
    val title: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter,
    val navigateTo: () -> Unit
)