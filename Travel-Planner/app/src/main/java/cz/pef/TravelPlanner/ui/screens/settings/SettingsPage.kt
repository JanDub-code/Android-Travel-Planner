package cz.pef.TravelPlanner.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.pef.TravelPlanner.R
import cz.pef.TravelPlanner.navigation.INavigationRouter
import cz.pef.TravelPlanner.ui.elements.BaseScreen

@Composable
fun SettingsScreen(
    navigation: INavigationRouter,
    selectedIndex: Int = 3 // Index pro nastavení
) {
    BaseScreen(
        topBarText = stringResource(id = R.string.settings),
        showLoading = false,
        navigationRouter = navigation,
        selectedIndex = selectedIndex
    ) {
            SettingsScreenContent(
                navigation = navigation
            )
        }
    }

@Composable
fun SettingsScreenContent(
    navigation: INavigationRouter
) {
    Column(modifier = Modifier.padding(16.dp)) {
        SettingsOption(
            title = stringResource(id = R.string.interests),
            onClick = { /* Navigace na záložku "Interests" */ navigation.navitageToInterests() }
        )
        SettingsOption(
            title = stringResource(id = R.string.profile),
            onClick = { /* Navigace na záložku "Profile" */ navigation.navigateToProfile() }
        )
        SettingsOption(
            title = stringResource(id = R.string.search),
            onClick = { /* Navigace na záložku "Search" */ navigation.navigateToSearch() }
        )
    }
}

@Composable
fun SettingsOption(
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        colors = CardColors(containerColor = Color(0xFFFF6F00), contentColor = Color.Black, disabledContentColor = Color.Gray, disabledContainerColor = Color.White),

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }
    }
}