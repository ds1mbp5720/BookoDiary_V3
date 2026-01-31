package lee.project.presentation.record

import androidx.compose.ui.text.input.TextFieldValue
import lee.project.domain.book.model.MyBookModel
import lee.project.domain.book.model.WishBookModel
import lee.project.presentation.UiEffect
import lee.project.presentation.UiEvent
import lee.project.presentation.UiState

enum class RecordType {
    MYBOOK, WISH
}

data class RecordUiState(
    val myBookList: List<MyBookModel> = emptyList(),
    val wishBookList: List<WishBookModel> = emptyList(),
    val myBookDetail: MyBookModel? = null,
    val recordVisibleType: RecordType = RecordType.MYBOOK,
    val query: TextFieldValue = TextFieldValue(""),
    val focused: Boolean = false,
    val searching: Boolean = false,
    val isLoading: Boolean = false
) : UiState

sealed interface RecordUiEvent : UiEvent {
    object LoadAllBooks : RecordUiEvent
    data class ChangeRecordType(val type: RecordType) : RecordUiEvent
    data class QueryChanged(val query: TextFieldValue) : RecordUiEvent
    data class FocusChanged(val focused: Boolean) : RecordUiEvent
    data class FindMyBook(val bookId: Long) : RecordUiEvent
    data class DeleteMyBook(val bookId: Long) : RecordUiEvent
    data class DeleteWishBook(val bookId: Long) : RecordUiEvent
    object ClearQuery : RecordUiEvent
}

sealed interface RecordUiEffect : UiEffect {
    data class ShowToast(val message: String) : RecordUiEffect
}
