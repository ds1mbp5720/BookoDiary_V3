package lee.project.presentation.home

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import lee.project.domain.book.usecase.GetAllMyBooksUseCase
import lee.project.domain.book.usecase.GetBookListPagingUseCase
import lee.project.domian.shared.LoadResult
import lee.project.presentation.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getBookListPagingUseCase: GetBookListPagingUseCase,
    private val getAllMyBooksUseCase: GetAllMyBooksUseCase
) : BaseViewModel<HomeScreenState, HomeScreenEvent, HomeScreenEffect>(
    initialState = HomeScreenState()
) {

    init {
        // 초기 데이터 로드
        onEvent(HomeScreenEvent.LoadHomeInitialData)
        onEvent(HomeScreenEvent.LoadMyBooks)
    }

    override suspend fun reduce(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.LoadHomeInitialData -> {
                loadAllHomePagingData()
            }
            is HomeScreenEvent.LoadCategoryPaging -> {
                loadSingleCategoryPaging(event.queryType, event.size)
            }
            is HomeScreenEvent.LoadMyBooks -> {
                loadMyBooks()
            }
        }
    }

    private fun loadAllHomePagingData() {
        setState {
            copy(
                bookListDataItemNewAll = getBookListPagingUseCase("ItemNewAll", 20).cachedIn(viewModelScope),
                bookListDataItemNewSpecial = getBookListPagingUseCase("ItemNewSpecial", 20).cachedIn(viewModelScope),
                bookListDataBestseller = getBookListPagingUseCase("Bestseller", 20).cachedIn(viewModelScope),
                bookListDataBlogBest = getBookListPagingUseCase("BlogBest", 20).cachedIn(viewModelScope)
            )
        }
    }

    private fun loadSingleCategoryPaging(queryType: String, size: Int) {
        setState {
            copy(
                singleCategoryBookList = getBookListPagingUseCase(queryType, size).cachedIn(viewModelScope)
            )
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
