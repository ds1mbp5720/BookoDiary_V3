package lee.project.presentation.home

import lee.project.domain.book.model.MyBookModel
import lee.project.presentation.UiEffect
import lee.project.presentation.UiEvent
import lee.project.presentation.UiState

data class HomeScreenState(
    val myBookList: List<MyBookModel> = emptyList(),
    val categoryQuery: String = "",
    val isLoading: Boolean = false
) : UiState

sealed class HomeScreenEvent : UiEvent {
    data class LoadCategoryPaging(val queryType: String, val size: Int = 20) : HomeScreenEvent()
    object LoadMyBooks : HomeScreenEvent()
}

sealed class HomeScreenEffect : UiEffect {
    data class ShowError(val message: String) : HomeScreenEffect()
}
