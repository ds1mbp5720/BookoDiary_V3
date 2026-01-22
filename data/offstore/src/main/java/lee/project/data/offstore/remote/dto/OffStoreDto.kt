package lee.project.data.offstore.remote.dto

import com.google.gson.annotations.SerializedName

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
)

data class OffStoreData(
    @SerializedName("offCode")
    val offCode: String,
    @SerializedName("offName")
    val offName: String,
    @SerializedName("link")
    val link: String,
)
