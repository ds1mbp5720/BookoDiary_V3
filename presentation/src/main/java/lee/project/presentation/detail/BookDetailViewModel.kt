package lee.project.presentation.detail

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import lee.project.domain.book.model.BookListModel
import lee.project.domain.book.model.MyBookModel
import lee.project.domain.book.model.WishBookModel
import lee.project.domain.book.usecase.GetBookDetailUseCase
import lee.project.domain.book.usecase.InsertMyBookUseCase
import lee.project.domain.book.usecase.InsertWishBookUseCase
import lee.project.domain.offstore.model.OffStoreListModel
import lee.project.domain.offstore.usecase.GetOffStoreInfoUseCase
import lee.project.domian.shared.LoadResult
import lee.project.presentation.BaseViewModel
import lee.project.presentation.UiEffect
import lee.project.presentation.UiEvent
import lee.project.presentation.UiState
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val getBookDetailUseCase: GetBookDetailUseCase,
    private val getOffStoreInfoUseCase: GetOffStoreInfoUseCase,
    private val insertMyBookUseCase: InsertMyBookUseCase,
    private val insertWishBookUseCase: InsertWishBookUseCase
) : BaseViewModel<BookDetailUiState, BookDetailUiEvent, BookDetailUiEffect>(BookDetailUiState()) {

    override suspend fun reduce(event: BookDetailUiEvent) {
        when (event) {
            is BookDetailUiEvent.LoadBookDetail -> fetchBookDetail(event.itemId)
            is BookDetailUiEvent.LoadOffStoreInfo -> fetchOffStoreInfo(event.itemId)
            is BookDetailUiEvent.AddMyBook -> addMyBook(event.book)
            is BookDetailUiEvent.AddWishBook -> addWishBook(event.book)
        }
    }

    private fun fetchBookDetail(itemId: Long) {
        getBookDetailUseCase(itemId)
            .onEach { result ->
                when (result) {
                    is LoadResult.Loading -> setState { copy(isLoading = true) }
                    is LoadResult.Success -> setState { copy(isLoading = false, bookDetail = result.data) }
                    is LoadResult.Error -> {
                        setState { copy(isLoading = false) }
                        sendEffect { BookDetailUiEffect.ShowToast(result.exception.message ?: "상세 정보를 가져오지 못했습니다.") }
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun fetchOffStoreInfo(itemId: String) {
        getOffStoreInfoUseCase(itemId)
            .onEach { result ->
                when (result) {
                    is LoadResult.Loading -> {}
                    is LoadResult.Success -> setState { copy(offStoreInfo = result.data) }
                    is LoadResult.Error -> {
                        sendEffect { BookDetailUiEffect.ShowToast("오프라인 재고 정보를 불러올 수 없습니다.") }
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun addMyBook(book: MyBookModel) {
        viewModelScope.launch {
            val result = insertMyBookUseCase(book)
            if (result is LoadResult.Success) {
                sendEffect { BookDetailUiEffect.ShowToast("기록에 추가되었습니다.") }
            } else {
                sendEffect { BookDetailUiEffect.ShowToast("추가에 실패했습니다.") }
            }
        }
    }

    private fun addWishBook(book: WishBookModel) {
        viewModelScope.launch {
            val result = insertWishBookUseCase(book)
            if (result is LoadResult.Success) {
                sendEffect { BookDetailUiEffect.ShowToast("찜 목록에 추가되었습니다.") }
            } else {
                sendEffect { BookDetailUiEffect.ShowToast("찜 추가에 실패했습니다.") }
            }
        }
    }
}
