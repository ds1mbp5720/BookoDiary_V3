package lee.project.data.book.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import lee.project.core.network.error.ErrorMapper
import lee.project.core.network.error.NetworkError
import lee.project.data.book.remote.dto.toDomain
import lee.project.data.book.remote.datasource.BookListDataSource
import lee.project.domain.book.model.BookModel

enum class ApiType {
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

            if (response.errorCode != null) {
                return LoadResult.Error(
                    NetworkError.BadRequest(response.errorMessage ?: "데이터 오류")
                )
            }
            val domainModel = response.toDomain()
            val books = domainModel.bookList

            if (books.isNotEmpty()) {
                LoadResult.Page(
                    data = books,
                    prevKey = if (currentPage == 1) null else currentPage - 1,
                    // 데이터가 비어있거나, 요청한 사이즈보다 적게 왔다면 다음 페이지 없음
                    nextKey = if (books.isEmpty()) null else currentPage + 1
                )
            } else {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }

        } catch (e: Exception) {
            LoadResult.Error(errorMapper.map(e))
        }
    }
}