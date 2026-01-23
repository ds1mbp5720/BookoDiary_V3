package lee.project.data.book.remote.dto

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

// 에러 응답 대응
interface AladinBaseResponse {
    val errorCode: Int?
    val errorMessage: String?
}

/**
 * 알라딘 API 공통 응답 포맷
 */
data class AladinResponse<T>(
    @SerializedName("title") val title: String = "",
    @SerializedName("link") val link: String = "",
    @SerializedName("logo") val logo: String = "",
    @SerializedName("pubDate") val pubDate: String = "",
    @SerializedName("totalResults") val totalResults: Int = 0,
    @SerializedName("startIndex") val startIndex: Int = 0,
    @SerializedName("itemsPerPage") val itemsPerPage: Int = 0,
    @SerializedName("query") val query: String = "",
    @SerializedName("version") val version: String = "",
    @SerializedName("searchCategoryId") val searchCategoryId: Int? = null,
    @SerializedName("searchCategoryName") val searchCategoryName: String = "",

    @SerializedName("item")
    val items: List<T> = emptyList(), // 데이터 리스트를 제네릭으로 포함
    @Json(name = "errorCode") override val errorCode: Int? = null,
    @Json(name = "errorMessage") override val errorMessage: String? = null
) : AladinBaseResponse