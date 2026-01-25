package lee.project.presentation.home

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import lee.project.domain.book.model.BookModel
import lee.project.domain.book.model.MyBookModel
import lee.project.presentation.UiEffect
import lee.project.presentation.UiEvent
import lee.project.presentation.UiState

data class HomeScreenState(
    val bookListDataItemNewAll: Flow<PagingData<BookModel>> = flowOf(PagingData.empty()),
    val bookListDataItemNewSpecial: Flow<PagingData<BookModel>> = flowOf(PagingData.empty()),
    val bookListDataBestseller: Flow<PagingData<BookModel>> = flowOf(PagingData.empty()),
    val bookListDataBlogBest: Flow<PagingData<BookModel>> = flowOf(PagingData.empty()),
    val singleCategoryBookList: Flow<PagingData<BookModel>> = flowOf(PagingData.empty()),
    val myBookList: List<MyBookModel> = emptyList(),
    val isLoading: Boolean = false
) : UiState

sealed class HomeScreenEvent : UiEvent {
    object LoadHomeInitialData : HomeScreenEvent()
    data class LoadCategoryPaging(val queryType: String, val size: Int = 20) : HomeScreenEvent()
    object LoadMyBooks : HomeScreenEvent()
}

sealed class HomeScreenEffect : UiEffect {
    data class ShowError(val message: String) : HomeScreenEffect()
}
