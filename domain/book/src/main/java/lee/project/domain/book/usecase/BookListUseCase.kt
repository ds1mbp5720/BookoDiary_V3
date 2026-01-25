package lee.project.domain.book.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lee.project.domain.book.model.BookListModel
import lee.project.domain.book.model.BookModel
import lee.project.domain.book.repository.BookListRepository
import lee.project.domian.shared.LoadResult
import lee.project.domian.shared.asResult
import javax.inject.Inject

class GetBookListUseCase @Inject constructor(
    private val bookListRepository: BookListRepository
) {
    operator fun invoke(queryType: String, start: Int): Flow<LoadResult<BookListModel>> =
        bookListRepository.getBookList(queryType, start).asResult()
}

class GetBookListPagingUseCase @Inject constructor(
    private val bookListRepository: BookListRepository
) {
    operator fun invoke(queryType: String, size: Int): Flow<PagingData<BookModel>> =
        bookListRepository.getBookListPaging(queryType, size)
}

class SearchBookListUseCase @Inject constructor(
    private val bookListRepository: BookListRepository
) {
    operator fun invoke(query: String): Flow<LoadResult<BookListModel>> =
        bookListRepository.searchBookList(query).asResult()
}

class GetSearchBookListPagingUseCase @Inject constructor(
    private val bookListRepository: BookListRepository
) {
    operator fun invoke(query: String, size: Int): Flow<PagingData<BookModel>> =
        bookListRepository.getSearchBookListPaging(query, size)
}

class GetBookDetailUseCase @Inject constructor(
    private val bookListRepository: BookListRepository
) {
    operator fun invoke(itemId: Long): Flow<LoadResult<BookListModel>> =
        bookListRepository.getBookDetail(itemId).asResult()
}