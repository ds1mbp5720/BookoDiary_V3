package lee.project.data.network.repositoryImpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lee.project.data.network.datasource.BookListDataSource
import lee.project.data.network.retrofit.book.toDomain
import lee.project.data.network.util.safeFlow
import lee.project.domain.book.model.BookListModel
import lee.project.domain.book.model.BookModel
import lee.project.domain.book.repository.BookListRepository
import javax.inject.Inject

class BookListRepositoryImpl @Inject constructor(
    private val bookListDataSource: BookListDataSource
) : BookListRepository {

    override fun getBookList(queryType: String, start: Int): Flow<BookListModel> = safeFlow {
        bookListDataSource.getBookList(queryType, start).toDomain()
    }

    override fun searchBookList(query: String): Flow<BookListModel> = safeFlow {
        bookListDataSource.searchBookList(query, 1).toDomain()
    }

    override fun getBookDetail(itemId: Long): Flow<BookListModel> = safeFlow {
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