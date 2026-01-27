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
    @SerializedName("title") @Json(name = "title") val title: String = "",
    @SerializedName("link") @Json(name = "link") val link: String = "",
    @SerializedName("logo") @Json(name = "logo") val logo: String = "",
    @SerializedName("pubDate") @Json(name = "pubDate") val pubDate: String = "",
    @SerializedName("totalResults") @Json(name = "totalResults") val totalResults: Int = 0,
    @SerializedName("startIndex") @Json(name = "startIndex") val startIndex: Int = 0,
    @SerializedName("itemsPerPage") @Json(name = "itemsPerPage") val itemsPerPage: Int = 0,
    @SerializedName("query") @Json(name = "query") val query: String = "",
    @SerializedName("version") @Json(name = "version") val version: String = "",
    @SerializedName("searchCategoryId") @Json(name = "searchCategoryId") val searchCategoryId: Int? = null,
    @SerializedName("searchCategoryName") @Json(name = "searchCategoryName") val searchCategoryName: String = "",

    @SerializedName("item")
    @Json(name = "item")
    val items: List<T> = emptyList(),

    @SerializedName("errorCode")
    @Json(name = "errorCode")
    override val errorCode: Int? = null,

    @SerializedName("errorMessage")
    @Json(name = "errorMessage")
    override val errorMessage: String? = null
) : AladinBaseResponse
