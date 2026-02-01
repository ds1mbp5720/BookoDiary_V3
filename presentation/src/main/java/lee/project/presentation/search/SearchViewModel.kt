package lee.project.presentation.search

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import lee.project.domain.book.model.BookModel
import lee.project.domain.book.usecase.GetSearchBookListPagingUseCase
import lee.project.domain.history.usecase.AddSearchHistoryUseCase
import lee.project.domain.history.usecase.ClearSearchHistoryUseCase
import lee.project.domain.history.usecase.GetSearchHistoryUseCase
import lee.project.domain.history.usecase.RemoveSearchHistoryUseCase
import lee.project.presentation.BaseViewModel
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchBookListPagingUseCase: GetSearchBookListPagingUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val addSearchHistoryUseCase: AddSearchHistoryUseCase,
    private val removeSearchHistoryUseCase: RemoveSearchHistoryUseCase,
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase
) : BaseViewModel<SearchUiState, SearchUiEvent, SearchUiEffect>(SearchUiState()) {
    // 검색 실행 트리거 (버튼 클릭 시에만 키워드를 방출)
    private val _searchTrigger = MutableSharedFlow<String>(replay = 1)
    val searchBookList: Flow<PagingData<BookModel>> = _searchTrigger
        .filter { it.isNotBlank() }
        .flatMapLatest { keyword ->
            getSearchBookListPagingUseCase(keyword, 20)
        }
        .cachedIn(viewModelScope)

    init {
        // 검색 기록 실시간 구독
        getSearchHistoryUseCase()
            .onEach { history ->
                setState { copy(searchHistory = history) }
            }
            .launchIn(viewModelScope)
    }

    override suspend fun reduce(event: SearchUiEvent) {
        when (event) {
            is SearchUiEvent.QueryChanged -> {
                setState { copy(query = event.query) }
            }
            is SearchUiEvent.FocusChanged -> {
                setState { copy(focused = event.focused) }
            }
            is SearchUiEvent.Search -> {
                // 버튼 클릭 시점에만 검색 트리거 작동
                _searchTrigger.emit(event.keyword)
                viewModelScope.launch {
                    addSearchHistoryUseCase(event.keyword)
                }
            }
            is SearchUiEvent.RemoveHistory -> {
                viewModelScope.launch {
                    removeSearchHistoryUseCase(event.keyword)
                }
            }
            is SearchUiEvent.ClearHistory -> {
                viewModelScope.launch {
                    clearSearchHistoryUseCase()
                }
            }
            is SearchUiEvent.ClickBookItem -> {
                sendEffect { SearchUiEffect.NavigateToBookDetail(event.bookId) }
            }
        }
    }
}
