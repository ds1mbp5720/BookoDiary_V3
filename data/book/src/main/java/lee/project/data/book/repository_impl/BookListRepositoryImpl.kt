package lee.project.data.book.repository_impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lee.project.core.network.util.safeNetworkFlow
import lee.project.data.book.remote.dto.toDomain
import lee.project.data.book.remote.datasource.BookListDataSource
import lee.project.data.book.remote.paging.ApiType
import lee.project.data.book.remote.paging.BookListPagingSource
import lee.project.domain.book.model.BookListModel
import lee.project.domain.book.model.BookModel
import lee.project.domain.book.repository.BookListRepository
import javax.inject.Inject

class BookListRepositoryImpl @Inject constructor(
    private val bookListDataSource: BookListDataSource
) : BookListRepository {

    override fun getBookList(queryType: String, start: Int): Flow<BookListModel> = safeNetworkFlow {
        bookListDataSource.getBookList(queryType, start).toDomain()
    }

    override fun searchBookList(query: String): Flow<BookListModel> = safeNetworkFlow {
        bookListDataSource.searchBookList(query, 1).toDomain()
    }

    override fun getBookDetail(itemId: Long): Flow<BookListModel> = safeNetworkFlow {
        bookListDataSource.getBookDetail(itemId).toDomain()
    }

    // Paging 부분 개선
    override fun getBookListPaging(queryType: String, size: Int): Flow<PagingData<BookModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = size,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                BookListPagingSource(queryType, bookListDataSource, ApiType.NORMAL)
            }
        ).flow
    }

    override fun getSearchBookListPaging(query: String, size: Int): Flow<PagingData<BookModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = size,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                BookListPagingSource(query, bookListDataSource, ApiType.SEARCH)
            }
        ).flow
    }
}