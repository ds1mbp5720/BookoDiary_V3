package lee.project.data.book.remote.datasource

import lee.project.data.book.remote.dto.AladinResponse
import lee.project.data.book.remote.book.BookData
import lee.project.data.book.remote.book.BookListApi
import javax.inject.Inject

class BookListDataSource @Inject constructor(
    private val bookListApi: BookListApi
) {
    // 1. 도서 리스트 가져오기 (신간, 베스트셀러 등)
    suspend fun getBookList(
        queryType: String,
        start: Int
    ): AladinResponse<BookData> {
        return bookListApi.getBookList(queryType = queryType, start = start)
    }

    // 2. 도서 검색하기
    suspend fun searchBookList(
        query: String,
        start: Int
    ): AladinResponse<BookData> {
        return bookListApi.searchBookList(query = query, start = start)
    }

    // 3. 도서 상세 정보 가져오기
    suspend fun getBookDetail(
        itemId: Long
    ): AladinResponse<BookData> {
        return bookListApi.getBookDetail(itemId = itemId)
    }
}