package lee.project.data.offstore.remote.dto

import com.google.gson.annotations.SerializedName

interface AladinBaseResponse {
    val errorCode: Int?
    val errorMessage: String?
}

data class OffStoreListData(
    @SerializedName("link")
    val link: String,
    @SerializedName("pubDate")
    val pubDate: String,
    @SerializedName("query")
    val query: String,
    @SerializedName("version")
    val version: String,
    @SerializedName("itemOffStoreList")
    val itemOffStoreList: List<OffStoreData>,
    @SerializedName("errorCode") override val errorCode: Int? = null,
    @SerializedName("errorMessage") override val errorMessage: String? = null
) : AladinBaseResponse

data class OffStoreData(
    @SerializedName("offCode")
    val offCode: String,
    @SerializedName("offName")
    val offName: String,
    @SerializedName("link")
    val link: String,
)
