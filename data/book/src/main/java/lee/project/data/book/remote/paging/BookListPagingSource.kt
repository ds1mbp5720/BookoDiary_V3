package lee.project.data.book.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
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
    private val apiType: ApiType
) : PagingSource<Int, BookModel>() {
    override fun getRefreshKey(state: PagingState<Int, BookModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    //검색, 일반 책 리스트 두 종류 Case 분류를 통해 load 함수 안에 구현
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookModel> {
        return try {
            val currentPage = params.key ?: 1
            val bookList = if (apiType == ApiType.SEARCH) {
                bookListDataSource.searchBookList(
                    query = query,
                    start = currentPage
                ).toDomain()
            } else {
                bookListDataSource.getBookList(
                    queryType = query,
                    start = currentPage
                ).toDomain()
            }
            if (bookList.bookList.isNotEmpty()) {
                LoadResult.Page(
                    data = bookList.bookList,
                    prevKey = if (currentPage == 1) null else currentPage - 1,
                    nextKey = if (bookList.bookList.isEmpty()) null else currentPage + 1
                )
            } else {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}