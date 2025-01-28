@file:OptIn(ExperimentalMaterial3Api::class)

package cz.pef.TravelPlanner.ui.elements

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cz.mendelu.pef.compose.todo.ui.elements.PlaceHolderScreen
import cz.mendelu.pef.compose.todo.ui.elements.PlaceholderScreenContent
import cz.pef.TravelPlanner.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(
    topBarText: String? = null,
    onBackClick: (() -> Unit)? = null,
    placeholderScreenContent: PlaceholderScreenContent? = null,
    showLoading: Boolean = false,
    floatingActionButton: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (paddingValues: PaddingValues) -> Unit) {

        Scaffold(
            floatingActionButton = floatingActionButton,
            topBar = {
                TopAppBar(
                    title = {
                        if (topBarText != null) {
                            Text(
                                text = topBarText,
                                style = MaterialTheme.typography.titleLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .padding(start = 0.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                    actions = actions,
                    navigationIcon = {
                        if (onBackClick != null) {
                            IconButton(onClick = onBackClick!!) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.back),
                                )
                            }
                        }
                    }
                )
            }
        ) {
            if (placeholderScreenContent != null) {
                PlaceHolderScreen(
                    content = placeholderScreenContent
                )
            } else if (showLoading) {
                LoadingScreen()
            } else {
                content(it)

            }
        }

}
