package lee.project.presentation.search

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lee.project.presentation.component.BookDiaryDivider
import lee.project.presentation.component.BookDiaryScaffold
import lee.project.presentation.component.BookDiarySurface
import lee.project.presentation.component.SearchBar
import lee.project.presentation.navigation.BookDiaryBottomBar
import lee.project.presentation.navigation.MainSections
import lee.project.presentation.theme.BookDiaryTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Search(
    onBookClick: (Long) -> Unit,
    onNavigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    val searchBookList = viewModel.searchBookList.collectAsLazyPagingItems()
    val isNoResult = searchBookList.loadState.refresh is LoadState.NotLoading &&
            searchBookList.itemCount == 0
    val isSearching = uiState.query.text.isNotEmpty() && searchBookList.loadState.refresh is LoadState.Loading
    var refreshingAction by remember { mutableStateOf(true) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = searchBookList.loadState.refresh is LoadState.Loading,
        onRefresh = {
            searchBookList.refresh()
            coroutine.launch {
                refreshingAction = false
                delay(1500)
                refreshingAction = true
            }
        }
    )

    // Side Effects 처리
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SearchUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Paging 상태에 따른 UI 업데이트 처리
    LaunchedEffect(searchBookList.loadState) {
        val loadState = searchBookList.loadState
        if (loadState.refresh is LoadState.Error) {
            val error = (loadState.refresh as LoadState.Error).error
            Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
        }
    }

    BookDiaryScaffold(
        bottomBar = {
            BookDiaryBottomBar(
                tabs = MainSections.entries.toTypedArray(),
                currentRoute = MainSections.SEARCH.route,
                navigateToRoute = onNavigateToRoute
            )
        },
        modifier = modifier
    ) { paddingValues ->
        BookDiarySurface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column {
                Spacer(modifier = Modifier.statusBarsPadding())

                SearchBar(
                    query = uiState.query,
                    onQueryChange = { viewModel.onEvent(SearchUiEvent.QueryChanged(it)) },
                    onSearch = {
                        viewModel.onEvent(SearchUiEvent.Search(uiState.query.text))
                    },
                    searchFocused = uiState.focused || uiState.query.text.isNotEmpty(),
                    onSearchFocusChange = { viewModel.onEvent(SearchUiEvent.FocusChanged(it)) },
                    onClearQuery = { viewModel.onEvent(SearchUiEvent.QueryChanged(TextFieldValue(""))) },
                    searching = isSearching,
                )

                BookDiaryDivider()

                Box(
                    modifier = Modifier
                        .background(BookDiaryTheme.colors.uiBackground)
                        .fillMaxWidth()
                        .height(if (isSearching) 90.dp else lerp(0.dp, 90.dp, pullRefreshState.progress.coerceIn(0f..1f)))
                ) {
                    if (isSearching) {
                        CircularProgressIndicator(
                            color = BookDiaryTheme.colors.brand,
                            modifier = Modifier
                                .padding(horizontal = 6.dp)
                                .align(Alignment.Center)
                                .size(70.dp)
                        )
                    }
                }

                // 검색 결과 또는 대기 화면 표시
                // 검색어가 있고 포커스되어 있거나 결과가 진행 중일 때 결과 화면 표시
                if (uiState.query.text.isNotEmpty()) {
                    SearchResultScreen(
                        books = searchBookList,
                        onBookClick = onBookClick,
                        searchResult = isNoResult,
                        resetSearchState = {
                            viewModel.onEvent(SearchUiEvent.QueryChanged(TextFieldValue("")))
                            viewModel.onEvent(SearchUiEvent.FocusChanged(false))
                        },
                        modifier = Modifier.pullRefresh(
                            state = pullRefreshState,
                            enabled = refreshingAction
                        )
                    )
                } else {
                    StandByScreen(viewModel)
                }
            }
        }
    }
}