package lee.project.data.offstore.repository_impl

import kotlinx.coroutines.flow.Flow
import lee.project.core.network.error.ErrorMapper
import lee.project.core.network.error.NetworkError
import lee.project.core.network.util.safeNetworkFlow
import lee.project.data.offstore.remote.datasource.OffStoreDataSource
import lee.project.data.offstore.remote.dto.toDomain
import lee.project.domain.offstore.model.OffStoreListModel
import lee.project.domain.offstore.repository.OffStoreRepository
import javax.inject.Inject

class OffStoreRepositoryImpl @Inject constructor(
    private val offStoreDataSource: OffStoreDataSource,
    private val errorMapper: ErrorMapper
) : OffStoreRepository {

    override fun getOffStoreInfo(itemId: String): Flow<OffStoreListModel> = safeNetworkFlow(errorMapper) {
        val response = offStoreDataSource.getOffStoreInfo(
            itemId = itemId,
            itemIdType = "itemId", // 필요 시 "ISBN13" 등으로 유연하게 대처
        )
        if (response.errorCode != null) {
            throw NetworkError.BadRequest(response.errorMessage ?: "매장 정보를 찾을 수 없습니다.")
        }

        response.toDomain()
    }
}