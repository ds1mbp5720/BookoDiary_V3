package lee.project.data.network.retrofit.store

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 해당 책의 보유 매장 정보
 */
interface OffStoreApi {
    @GET("ItemOffStoreList.aspx")
    suspend fun getOffStoreInfo(
        @Query("ItemId") itemId: String, // 리스트 종류
        @Query("ItemIdType") itemIdType: String,
        @Query("output") output: String = "js"
    ): OffStoreListData
}