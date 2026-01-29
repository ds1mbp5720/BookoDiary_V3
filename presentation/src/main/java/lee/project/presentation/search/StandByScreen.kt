package lee.project.presentation.search

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import lee.project.presentation.R
import lee.project.presentation.component.BasicButton
import lee.project.presentation.theme.BookDiaryTheme

@Composable
fun StandByScreen(
    searchViewModel: SearchViewModel
) {
    val uiState by searchViewModel.uiState.collectAsStateWithLifecycle()
    var historyMode by remember { mutableStateOf(false) }

    if (uiState.searchHistory.isEmpty()) {
        Text(
            modifier = Modifier.fillMaxSize().padding(top = 32.dp),
            text = stringResource(id = R.string.str_no_search_record),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = BookDiaryTheme.colors.textHelp
        )
    } else {
        Column(modifier = Modifier.padding(top = 8.dp)) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.align(Alignment.Center).fillMaxWidth(),
                    text = stringResource(id = R.string.str_search_record),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = BookDiaryTheme.colors.textPrimary
                )

                BasicButton(
                    onClick = { historyMode = !historyMode },
                    modifier = Modifier.align(Alignment.CenterEnd).padding(end = 16.dp),
                    border = BorderStroke(1.dp, BookDiaryTheme.colors.brand),
                    backgroundGradient = listOf(BookDiaryTheme.colors.uiBackground, BookDiaryTheme.colors.uiBackground)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "change_list_mode",
                        tint = BookDiaryTheme.colors.brand,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            if (historyMode) {
                SearchHistoryColumn(
                    searchHistory = uiState.searchHistory,
                    onDelete = { searchViewModel.onEvent(SearchUiEvent.RemoveHistory(it)) },
                    onClick = { 
                        searchViewModel.onEvent(SearchUiEvent.QueryChanged(TextFieldValue(it)))
                        searchViewModel.onEvent(SearchUiEvent.Search(it))
                    }
                )
            } else {
                SearchHistoryFlow(
                    searchHistory = uiState.searchHistory,
                    onDelete = { searchViewModel.onEvent(SearchUiEvent.RemoveHistory(it)) },
                    onClick = {
                        searchViewModel.onEvent(SearchUiEvent.QueryChanged(TextFieldValue(it)))
                        searchViewModel.onEvent(SearchUiEvent.Search(it))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchHistoryFlow(
    searchHistory: List<String>,
    onDelete: (String) -> Unit,
    onClick: (String) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        searchHistory.forEach { keyword ->
            SearchHistoryCard(
                search = keyword,
                deleteButtonVisible = true,
                onDeleteEvent = { onDelete(keyword) },
                onClickEvent = { onClick(keyword) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchHistoryColumn(
    searchHistory: List<String>,
    onDelete: (String) -> Unit,
    onClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(searchHistory, key = { it }) { keyword ->
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = {
                    if (it == SwipeToDismissBoxValue.EndToStart) {
                        onDelete(keyword)
                        true
                    } else false
                }
            )

            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {
                    val color by animateColorAsState(
                        when (dismissState.targetValue) {
                            SwipeToDismissBoxValue.EndToStart -> Color.Red.copy(alpha = 0.8f)
                            else -> Color.Transparent
                        }, label = "delete_bg"
                    )
                    Box(
                        Modifier.fillMaxSize().background(color, RoundedCornerShape(16.dp)).padding(horizontal = 20.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                    }
                },
                enableDismissFromStartToEnd = false
            ) {
                SearchHistoryCard(
                    modifier = Modifier.fillMaxWidth(),
                    search = keyword,
                    onClickEvent = { onClick(keyword) }
                )
            }
        }
    }
}

@Composable
fun SearchHistoryCard(
    modifier: Modifier = Modifier,
    search: String,
    deleteButtonVisible: Boolean = false,
    onDeleteEvent: () -> Unit = {},
    onClickEvent: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClickEvent() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = BookDiaryTheme.colors.uiBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = search,
                style = MaterialTheme.typography.bodyMedium,
                color = BookDiaryTheme.colors.textPrimary
            )
            if (deleteButtonVisible) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    modifier = Modifier.size(18.dp).clickable { onDeleteEvent() },
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "delete",
                    tint = BookDiaryTheme.colors.textHelp
                )
            }
        }
    }
}
