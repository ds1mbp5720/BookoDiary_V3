package lee.project.domain.book.repository

import kotlinx.coroutines.flow.Flow
import lee.project.domain.book.model.MyBookModel
import lee.project.domain.book.model.WishBookModel

interface BookLibraryRepository {
    suspend fun insertWishBook(book: WishBookModel)
    fun getAllWishBooks(): Flow<List<WishBookModel>>
    fun searchWishBooks(query: String): Flow<List<WishBookModel>>
    suspend fun deleteWishBook(itemId: String)

    suspend fun insertMyBook(book: MyBookModel)
    fun getAllMyBooks(): Flow<List<MyBookModel>>
    fun searchMyBooks(query: String): Flow<List<MyBookModel>>
    suspend fun deleteMyBook(itemId: String)
}