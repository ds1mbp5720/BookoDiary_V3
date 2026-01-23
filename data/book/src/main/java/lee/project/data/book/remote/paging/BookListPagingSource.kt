package lee.project.data.book.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import lee.project.core.network.error.ErrorMapper
import lee.project.core.network.error.NetworkError
import lee.project.data.book.remote.dto.toDomain
import lee.project.data.book.remote.datasource.BookListDataSource
import lee.project.domain.book.model.BookModel
import retrofit2.HttpException
import java.io.IOException

enum class ApiType{
    SEARCH, NORMAL
}

class BookListPagingSource(
    private val query: String = "",
    private val bookListDataSource: BookListDataSource,
    private val apiType: ApiType,
    private val errorMapper: ErrorMapper
) : PagingSource<Int, BookModel>() {
    override fun getRefreshKey(state: PagingState<Int, BookModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookModel> {
        val currentPage = params.key ?: 1

        return try {
            val response = if (apiType == ApiType.SEARCH) {
                bookListDataSource.searchBookList(query = query, start = currentPage)
            } else {
                bookListDataSource.getBookList(queryType = query, start = currentPage)
            }

            // 알라딘 비즈니스 에러 체크 (200 OK 내부에 errorCode가 있는 경우)
            if (response.errorCode != null) {
                return LoadResult.Error(
                    NetworkError.BadRequest(response.errorMessage ?: "데이터 오류")
                )
            }

            val domainModel = response.toDomain()
            val books = domainModel.bookList

            LoadResult.Page(
                data = books,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (books.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(errorMapper.map(e))
        }
    }
}