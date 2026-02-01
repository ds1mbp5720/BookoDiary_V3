package lee.project.presentation.record

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import lee.project.domain.book.usecase.DeleteMyBookUseCase
import lee.project.domain.book.usecase.DeleteWishBookUseCase
import lee.project.domain.book.usecase.GetAllMyBooksUseCase
import lee.project.domain.book.usecase.GetAllWishBooksUseCase
import lee.project.domian.shared.LoadResult
import lee.project.presentation.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val getAllMyBooksUseCase: GetAllMyBooksUseCase,
    private val getAllWishBooksUseCase: GetAllWishBooksUseCase,
    private val deleteMyBookUseCase: DeleteMyBookUseCase,
    private val deleteWishBookUseCase: DeleteWishBookUseCase
) : BaseViewModel<RecordUiState, RecordUiEvent, RecordUiEffect>(RecordUiState()) {

    init {
        observeMyBooks()
        observeWishBooks()
    }

    override suspend fun reduce(event: RecordUiEvent) {
        when (event) {
            is RecordUiEvent.LoadAllBooks -> {
                // Subscription is handled in init
            }
            is RecordUiEvent.ChangeRecordType -> {
                setState { copy(recordVisibleType = event.type) }
            }
            is RecordUiEvent.QueryChanged -> {
                setState { copy(query = event.query, searching = event.query.text.isNotEmpty()) }
            }
            is RecordUiEvent.FocusChanged -> {
                setState { copy(focused = event.focused) }
            }
            is RecordUiEvent.FindMyBook -> {
                val book = uiState.value.myBookList.find { it.itemId == event.bookId }
                setState { copy(myBookDetail = book) }
                // 기록 상세 화면 이동 효과 전송
                sendEffect { RecordUiEffect.NavigateToMyBookDetail(event.bookId) }
            }
            is RecordUiEvent.ClickWishBook -> {
                sendEffect { RecordUiEffect.NavigateToWishBookDetail(event.bookId) }
            }
            is RecordUiEvent.ClickMyBook -> {
                sendEffect { RecordUiEffect.NavigateToWishBookDetail(event.bookId) }
            }
            is RecordUiEvent.DeleteMyBook -> {
                deleteMyBook(event.bookId)
            }
            is RecordUiEvent.DeleteWishBook -> {
                deleteWishBook(event.bookId)
            }
            is RecordUiEvent.ClearQuery -> {
                setState { copy(query = androidx.compose.ui.text.input.TextFieldValue(""), searching = false) }
            }
        }
    }

    private fun observeMyBooks() {
        getAllMyBooksUseCase()
            .onEach { result ->
                when (result) {
                    is LoadResult.Loading -> setState { copy(isLoading = true) }
                    is LoadResult.Success -> setState { copy(myBookList = result.data, isLoading = false) }
                    is LoadResult.Error -> {
                        setState { copy(isLoading = false) }
                        sendEffect { RecordUiEffect.ShowToast(result.exception.message ?: "내 책 목록 로드 실패") }
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun observeWishBooks() {
        getAllWishBooksUseCase()
            .onEach { result ->
                when (result) {
                    is LoadResult.Loading -> setState { copy(isLoading = true) }
                    is LoadResult.Success -> setState { copy(wishBookList = result.data, isLoading = false) }
                    is LoadResult.Error -> {
                        setState { copy(isLoading = false) }
                        sendEffect { RecordUiEffect.ShowToast(result.exception.message ?: "찜 목록 로드 실패") }
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun deleteMyBook(bookId: Long) {
        viewModelScope.launch {
            val result = deleteMyBookUseCase(bookId.toString())
            if (result is LoadResult.Error) {
                sendEffect { RecordUiEffect.ShowToast("삭제 실패") }
            }
        }
    }

    private fun deleteWishBook(bookId: Long) {
        viewModelScope.launch {
            val result = deleteWishBookUseCase(bookId.toString())
            if (result is LoadResult.Error) {
                sendEffect { RecordUiEffect.ShowToast("삭제 실패") }
            }
        }
    }
}
