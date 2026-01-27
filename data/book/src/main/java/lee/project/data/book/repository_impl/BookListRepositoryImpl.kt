package lee.project.data.book.repository_impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lee.project.core.network.error.ErrorMapper
import lee.project.core.network.error.NetworkError
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
    private val bookListDataSource: BookListDataSource,
    private val errorMapper: ErrorMapper
) : BookListRepository {

    override fun getBookList(queryType: String, start: Int): Flow<BookListModel> =
        safeNetworkFlow(errorMapper) {
            val response = bookListDataSource.getBookList(queryType, start)

            if (response.errorCode != null) {
                throw NetworkError.BadRequest(response.errorMessage ?: "데이터를 가져오지 못했습니다.")
            }

            response.toDomain()
        }

    override fun searchBookList(query: String): Flow<BookListModel> = safeNetworkFlow(errorMapper) {
        val response = bookListDataSource.searchBookList(query, 1)

        if (response.errorCode != null) {
            throw NetworkError.BadRequest(response.errorMessage ?: "검색 결과가 없습니다.")
        }

        response.toDomain()
    }

    override fun getBookDetail(itemId: Long): Flow<BookListModel> = safeNetworkFlow(errorMapper) {
        val response = bookListDataSource.getBookDetail(itemId)

        if (response.errorCode != null) {
            throw NetworkError.BadRequest(response.errorMessage ?: "상세 정보를 찾을 수 없습니다.")
        }

        response.toDomain()
    }

    // Paging 부분 개선
    override fun getBookListPaging(queryType: String, size: Int): Flow<PagingData<BookModel>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                BookListPagingSource(
                    query = queryType,
                    bookListDataSource = bookListDataSource,
                    apiType = ApiType.NORMAL,
                    errorMapper = errorMapper
                )
            }
        ).flow
    }

    override fun getSearchBookListPaging(query: String, size: Int): Flow<PagingData<BookModel>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                BookListPagingSource(
                    query = query,
                    bookListDataSource = bookListDataSource,
                    apiType = ApiType.SEARCH,
                    errorMapper = errorMapper
                )
            }
        ).flow
    }
}