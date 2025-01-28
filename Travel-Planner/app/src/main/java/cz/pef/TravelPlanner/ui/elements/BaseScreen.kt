@file:OptIn(ExperimentalMaterial3Api::class)

package cz.pef.TravelPlanner.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cz.mendelu.pef.compose.todo.ui.elements.PlaceHolderScreen
import cz.mendelu.pef.compose.todo.ui.elements.PlaceholderScreenContent
import cz.pef.TravelPlanner.R
import cz.pef.TravelPlanner.navigation.INavigationRouter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    topBarText: String? = null,
    onBackClick: (() -> Unit)? = null,
    placeholderScreenContent: PlaceholderScreenContent? = null,
    showLoading: Boolean = false,
    navigationRouter: INavigationRouter,
    selectedIndex: Int,
    floatingActionButton: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {
    Scaffold(
        floatingActionButton = floatingActionButton,
        topBar = {
            TopAppBar(
                title = {
                    if (topBarText != null) {
                        Text(
                            text = topBarText,
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(start = 0.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2563EB)),
                actions = actions,
                navigationIcon = {
                    if (onBackClick != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .clickable { onBackClick() } // Kliknutelná celá oblast
                                .padding(end = 16.dp)
                                .testTag("Back")
                        ) {
                            if (selectedIndex == 0) {
                                Image(
                                    painter = painterResource(id = R.drawable.refresh_white), // Vložte svůj zdroj pro ikonu
                                    contentDescription = "Save",
                                    modifier = Modifier
                                        .size(20.dp)

                                )
                                Spacer(modifier = Modifier.width(8.dp)) // Mezera mezi ikonou a textem
                                Text(
                                    text = stringResource(R.string.refresh_results),//stringResource(R.string.back),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.White, // Text je bílý
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }else{
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                                tint = Color.White // Ikona je bílá
                            )
                            Spacer(modifier = Modifier.width(8.dp)) // Mezera mezi ikonou a textem
                            Text(
                                text = stringResource(R.string.back),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White, // Text je bílý
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                        }
                    }
                }
            )
        },
        bottomBar = {
            OwnNavBar(navigationRouter = navigationRouter, selectedIndex = selectedIndex)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2563EB)) // Nastavení modrého pozadí pro hlavní obrazovku
                .padding(paddingValues)
        ) {
            if (placeholderScreenContent != null) {
                PlaceHolderScreen(
                    content = placeholderScreenContent
                )
            } else if (showLoading) {
                LoadingScreen()
            } else {
                content(paddingValues)
            }
        }
    }
}
