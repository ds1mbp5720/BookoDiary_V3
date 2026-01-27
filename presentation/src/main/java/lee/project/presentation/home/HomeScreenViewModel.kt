package lee.project.presentation.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import lee.project.domain.book.model.BookModel
import lee.project.domain.book.usecase.GetAllMyBooksUseCase
import lee.project.domain.book.usecase.GetBookListPagingUseCase
import lee.project.domian.shared.LoadResult
import lee.project.presentation.BaseViewModel
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getBookListPagingUseCase: GetBookListPagingUseCase,
    private val getAllMyBooksUseCase: GetAllMyBooksUseCase
) : BaseViewModel<HomeScreenState, HomeScreenEvent, HomeScreenEffect>(
    initialState = HomeScreenState()
) {
    val bookListDataItemNewAll: Flow<PagingData<BookModel>> = 
        getBookListPagingUseCase("ItemNewAll", 20).cachedIn(viewModelScope)
    
    val bookListDataItemNewSpecial: Flow<PagingData<BookModel>> = 
        getBookListPagingUseCase("ItemNewSpecial", 20).cachedIn(viewModelScope)
    
    val bookListDataBestseller: Flow<PagingData<BookModel>> = 
        getBookListPagingUseCase("Bestseller", 20).cachedIn(viewModelScope)
    
    val bookListDataBlogBest: Flow<PagingData<BookModel>> = 
        getBookListPagingUseCase("BlogBest", 20).cachedIn(viewModelScope)

    // 상세 카테고리 리스트 (상태의 query를 기반으로 변환)
    val singleCategoryBookList: Flow<PagingData<BookModel>> = uiState
        .map { it.categoryQuery }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                getBookListPagingUseCase("ItemNewAll", 20)
            } else {
                getBookListPagingUseCase(query, 20)
            }
        }
        .cachedIn(viewModelScope)

    init {
        onEvent(HomeScreenEvent.LoadMyBooks)
    }

    override suspend fun reduce(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.LoadCategoryPaging -> {
                setState { copy(categoryQuery = event.queryType) }
            }
            is HomeScreenEvent.LoadMyBooks -> {
                loadMyBooks()
            }
            else -> {}
        }
    }

    private fun loadMyBooks() {
        getAllMyBooksUseCase()
            .onEach { result ->
                when (result) {
                    is LoadResult.Success -> {
                        setState { copy(myBookList = result.data, isLoading = false) }
                    }
                    is LoadResult.Error -> {
                        setState { copy(isLoading = false) }
                        sendEffect { HomeScreenEffect.ShowError(result.exception.message ?: "데이터 로드 실패") }
                    }
                    is LoadResult.Loading -> {
                        setState { copy(isLoading = true) }
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}
