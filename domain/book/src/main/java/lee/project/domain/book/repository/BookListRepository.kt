package lee.project.domain.book.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lee.project.domain.book.model.BookListModel
import lee.project.domain.book.model.BookModel

interface BookListRepository {
    fun getBookList(queryType: String, start: Int): Flow<BookListModel>
    fun getBookListPaging(queryType: String, size: Int): Flow<PagingData<BookModel>>
    fun searchBookList(query: String): Flow<BookListModel>
    fun getSearchBookListPaging(query: String, size: Int): Flow<PagingData<BookModel>>
    fun getBookDetail(itemId: Long): Flow<BookListModel>
}