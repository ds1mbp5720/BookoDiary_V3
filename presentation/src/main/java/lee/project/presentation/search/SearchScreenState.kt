package lee.project.presentation.search

import androidx.compose.ui.text.input.TextFieldValue
import lee.project.presentation.UiEffect
import lee.project.presentation.UiEvent
import lee.project.presentation.UiState

data class SearchUiState(
    val query: TextFieldValue = TextFieldValue(""),
    val focused: Boolean = false,
    val searchHistory: List<String> = emptyList()
) : UiState

sealed interface SearchUiEvent : UiEvent {
    data class QueryChanged(val query: TextFieldValue) : SearchUiEvent
    data class FocusChanged(val focused: Boolean) : SearchUiEvent
    data class Search(val keyword: String) : SearchUiEvent
    data class ClickBookItem(val bookId: Long) : SearchUiEvent
    data class RemoveHistory(val keyword: String) : SearchUiEvent
    object ClearHistory : SearchUiEvent
}

sealed interface SearchUiEffect : UiEffect {
    data class ShowToast(val message: String) : SearchUiEffect
    data class NavigateToBookDetail(val bookId: Long) : SearchUiEffect
}