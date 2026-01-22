package lee.project.data.book.remote

import lee.project.data.book.remote.dto.AladinResponse
import lee.project.data.book.remote.dto.BookData
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 책 정보 호출
 */
interface BookListApi {
    /**
     * "QueryType" 값 종류
     * ItemNewAll : 신간 전체 리스트
     * ItemNewSpecial : 주목할 만한 신간 리스트
     * Bestseller : 베스트셀러
     * BlogBest : 블로거 베스트셀러 (국내도서만 조회 가능)
     */
    @GET("ItemList.aspx")
    suspend fun getBookList(
        @Query("QueryType") queryType: String, // 리스트 종류
        @Query("start") start: Int,
        @Query("Cover") cover: String = "MidBig",
        @Query("SearchTarget") searchTarget: String = "Book",
        @Query("output") output: String = "js",
        @Query("Version") version: String = "20131101"
    ): AladinResponse<BookData>

    @GET("ItemSearch.aspx")
    suspend fun searchBookList(
        @Query("Query") query: String, // 검색어
        @Query("QueryType") queryType: String = "Keyword ", // Keyword : 제목 + 저자, Publisher : 출판사
        @Query("SearchTarget") searchTarget: String = "All",
        @Query("start") start: Int,
        @Query("output") output: String = "js",
        @Query("Version") version: String = "20131101"
    ): AladinResponse<BookData>

    @GET("ItemLookUp.aspx")
    suspend fun getBookDetail(
        @Query("ItemId") itemId: Long,
        @Query("itemIdType") itemIdType: String = "ItemId",
        @Query("Cover") cover: String = "Big",
        @Query("OptResult") optResult: Array<String> = arrayOf("cardReviewImgList", "ratingInfo", "bestSellerRank"),
        @Query("output") output: String = "js",
        @Query("Version") version: String = "20131101"
    ): AladinResponse<BookData>
}