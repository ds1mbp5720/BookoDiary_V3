package lee.project.domain.book.usecase

import kotlinx.coroutines.flow.Flow
import lee.project.domain.book.model.MyBookModel
import lee.project.domain.book.model.WishBookModel
import lee.project.domain.book.repository.BookLibraryRepository
import lee.project.domian.shared.LoadResult
import lee.project.domian.shared.asResult
import javax.inject.Inject

class InsertWishBookUseCase @Inject constructor(private val repository: BookLibraryRepository) {
    suspend operator fun invoke(book: WishBookModel): LoadResult<Unit> = runCatching {
        repository.insertWishBook(book)
    }.fold(
        onSuccess = { LoadResult.Success(Unit) },
        onFailure = { LoadResult.Error(it) }
    )
}

class GetAllWishBooksUseCase @Inject constructor(private val repository: BookLibraryRepository) {
    operator fun invoke(): Flow<LoadResult<List<WishBookModel>>> =
        repository.getAllWishBooks().asResult()
}

class SearchWishBooksUseCase @Inject constructor(private val repository: BookLibraryRepository) {
    operator fun invoke(query: String): Flow<LoadResult<List<WishBookModel>>> =
        repository.searchWishBooks(query).asResult()
}

class DeleteWishBookUseCase @Inject constructor(private val repository: BookLibraryRepository) {
    suspend operator fun invoke(itemId: String): LoadResult<Unit> = runCatching {
        repository.deleteWishBook(itemId)
    }.fold(
        onSuccess = { LoadResult.Success(Unit) },
        onFailure = { LoadResult.Error(it) }
    )
}

class InsertMyBookUseCase @Inject constructor(private val repository: BookLibraryRepository) {
    suspend operator fun invoke(book: MyBookModel): LoadResult<Unit> = runCatching {
        repository.insertMyBook(book)
    }.fold(
        onSuccess = { LoadResult.Success(Unit) },
        onFailure = { LoadResult.Error(it) }
    )
}

class GetAllMyBooksUseCase @Inject constructor(private val repository: BookLibraryRepository) {
    operator fun invoke(): Flow<LoadResult<List<MyBookModel>>> =
        repository.getAllMyBooks().asResult()
}

class SearchMyBooksUseCase @Inject constructor(private val repository: BookLibraryRepository) {
    operator fun invoke(query: String): Flow<LoadResult<List<MyBookModel>>> =
        repository.searchMyBooks(query).asResult()
}

class DeleteMyBookUseCase @Inject constructor(private val repository: BookLibraryRepository) {
    suspend operator fun invoke(itemId: String): LoadResult<Unit> = runCatching {
        repository.deleteMyBook(itemId)
    }.fold(
        onSuccess = { LoadResult.Success(Unit) },
        onFailure = { LoadResult.Error(it) }
    )
}
