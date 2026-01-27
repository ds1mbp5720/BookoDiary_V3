package lee.project.data.book.remote.dto

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class BookDto(
    @SerializedName("itemId") @Json(name = "itemId") val itemId: String = "",
    @SerializedName("title") @Json(name = "title") val title: String = "",
    @SerializedName("link") @Json(name = "link") val link: String = "",
    @SerializedName("author") @Json(name = "author") val author: String = "",
    @SerializedName("pubDate") @Json(name = "pubDate") val pubDate: String = "",
    @SerializedName("description") @Json(name = "description") val description: String = "",
    @SerializedName("isbn") @Json(name = "isbn") val isbn: String = "",
    @SerializedName("isbn13") @Json(name = "isbn13") val isbn13: String = "",
    @SerializedName("priceSales") @Json(name = "priceSales") val priceSales: String = "",
    @SerializedName("priceStandard") @Json(name = "priceStandard") val priceStandard: String = "",
    @SerializedName("mallType") @Json(name = "mallType") val mallType: String = "",
    @SerializedName("stockStatus") @Json(name = "stockStatus") val stockStatus: String? = "",
    @SerializedName("mileage") @Json(name = "mileage") val mileage: String = "",
    @SerializedName("cover") @Json(name = "cover") val cover: String = "",
    @SerializedName("categoryId") @Json(name = "categoryId") val categoryId: String = "",
    @SerializedName("categoryName") @Json(name = "categoryName") val categoryName: String = "",
    @SerializedName("publisher") @Json(name = "publisher") val publisher: String = "",
    @SerializedName("salesPoint") @Json(name = "salesPoint") val salesPoint: String = "",
    @SerializedName("adult") @Json(name = "adult") val adult: Boolean = false,
    @SerializedName("fixedPrice") @Json(name = "fixedPrice") val fixedPrice: Boolean = false,
    @SerializedName("customerReviewRank") @Json(name = "customerReviewRank") val customerReviewRank: String = "",
    @SerializedName("subInfo") @Json(name = "subInfo") val subInfo: SubInfo? = null
)

data class SubInfo(
    @SerializedName("subTitle") @Json(name = "subTitle") val subTitle: String = "",
    @SerializedName("originalTitle") @Json(name = "originalTitle") val originalTitle: String = "",
    @SerializedName("itemPage") @Json(name = "itemPage") val itemPage: String = "",
    @SerializedName("subbarcode") @Json(name = "subbarcode") val subbarcode: String = "",
    @SerializedName("cardReviewImgList") @Json(name = "cardReviewImgList")
    val cardReviewImgList: List<String>? = emptyList(), // Nullable 및 기본값 추가하여 누락 시 에러 방지
    @SerializedName("ratingInfo") @Json(name = "ratingInfo") val ratingInfo: RatingInfo? = null,
    @SerializedName("bestSellerRank") @Json(name = "bestSellerRank") val bestSellerRank: String = ""
)

data class RatingInfo(
    @SerializedName("ratingScore") @Json(name = "ratingScore") val ratingScore: String = "",
    @SerializedName("ratingCount") @Json(name = "ratingCount") val ratingCount: String = "",
    @SerializedName("commentReviewCount") @Json(name = "commentReviewCount") val commentReviewCount: String = "",
    @SerializedName("myReviewCount") @Json(name = "myReviewCount") val myReviewCount: String = "",
)
