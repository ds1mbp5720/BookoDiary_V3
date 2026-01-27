package lee.project.presentation.detail

import lee.project.domain.book.model.BookListModel
import lee.project.domain.book.model.MyBookModel
import lee.project.domain.book.model.WishBookModel
import lee.project.domain.offstore.model.OffStoreListModel
import lee.project.presentation.UiEffect
import lee.project.presentation.UiEvent
import lee.project.presentation.UiState

data class BookDetailUiState(
    val bookDetail: BookListModel? = null,
    val offStoreInfo: OffStoreListModel? = null,
    val isLoading: Boolean = false
) : UiState

sealed interface BookDetailUiEvent : UiEvent {
    data class LoadBookDetail(val itemId: Long) : BookDetailUiEvent
    data class LoadOffStoreInfo(val itemId: String) : BookDetailUiEvent
    data class AddMyBook(val book: MyBookModel) : BookDetailUiEvent
    data class AddWishBook(val book: WishBookModel) : BookDetailUiEvent
}

sealed interface BookDetailUiEffect : UiEffect {
    data class ShowToast(val message: String) : BookDetailUiEffect
}