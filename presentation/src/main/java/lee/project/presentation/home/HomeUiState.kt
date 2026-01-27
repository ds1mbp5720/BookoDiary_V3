package lee.project.presentation.home

import lee.project.domain.book.model.MyBookModel
import lee.project.presentation.UiEffect
import lee.project.presentation.UiEvent
import lee.project.presentation.UiState

data class HomeUiState(
    val myBookList: List<MyBookModel> = emptyList(),
    val categoryQuery: String = "",
    val isLoading: Boolean = false
) : UiState

sealed class HomeUiEvent : UiEvent {
    data class LoadCategoryPaging(val queryType: String, val size: Int = 20) : HomeUiEvent()
    object LoadMyBooks : HomeUiEvent()
}

sealed class HomeUiEffect : UiEffect {
    data class ShowError(val message: String) : HomeUiEffect()
}
