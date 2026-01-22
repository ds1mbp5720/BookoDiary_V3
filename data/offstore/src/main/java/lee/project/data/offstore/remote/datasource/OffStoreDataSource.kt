package lee.project.data.offstore.remote.datasource

import lee.project.data.offstore.remote.OffStoreApi
import lee.project.data.offstore.remote.dto.OffStoreListData
import javax.inject.Inject

class OffStoreDataSource @Inject constructor(
    private val offStoreApi: OffStoreApi
) {
    //특정 도서의 오프라인 매장 재고 정보 조회
    suspend fun getOffStoreInfo(
        itemId: String,
        itemIdType: String = "ISBN13"
    ): OffStoreListData {
        return offStoreApi.getOffStoreInfo(
            itemId = itemId,
            itemIdType = itemIdType
        )
    }
}