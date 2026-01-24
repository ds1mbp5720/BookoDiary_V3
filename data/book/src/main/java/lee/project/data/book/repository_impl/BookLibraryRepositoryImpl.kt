package lee.project.data.book.repository_impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import lee.project.data.book.local.dao.BookDao
import lee.project.data.book.local.entity.toDomain
import lee.project.data.book.local.entity.toEntity
import lee.project.domain.book.model.MyBookModel
import lee.project.domain.book.model.WishBookModel
import lee.project.domain.book.repository.BookLibraryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookLibraryRepositoryImpl @Inject constructor(
    private val bookDao: BookDao
) : BookLibraryRepository {
    override suspend fun insertWishBook(book: WishBookModel) {
        // Domain Model -> Entity 변환 후 삽입
        bookDao.insertWishBook(book.toEntity())
    }

    override fun getAllWishBooks(): Flow<List<WishBookModel>> {
        return bookDao.getAllWishBooks()
            .map { entities ->
                entities.map { it.toDomain() }
            }
            .flowOn(Dispatchers.IO)
    }

    override fun searchWishBooks(query: String): Flow<List<WishBookModel>> {
        return bookDao.searchWishBooks(query)
            .map { entities ->
                entities.map { it.toDomain() }
            }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun deleteWishBook(itemId: String) {
        bookDao.deleteWishBookById(itemId)
    }

    // --- MyBook (내 서재) ---

    override suspend fun insertMyBook(book: MyBookModel) {
        bookDao.insertMyBook(book.toEntity())
    }

    override fun getAllMyBooks(): Flow<List<MyBookModel>> {
        return bookDao.getAllMyBooks()
            .map { entities ->
                entities.map { it.toDomain() }
            }
            .flowOn(Dispatchers.IO)
    }

    override fun searchMyBooks(query: String): Flow<List<MyBookModel>> {
        return bookDao.searchMyBooks(query)
            .map { entities ->
                entities.map { it.toDomain() }
            }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun deleteMyBook(itemId: String) {
        bookDao.deleteMyBookById(itemId)
    }
}