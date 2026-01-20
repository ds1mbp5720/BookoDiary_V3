package lee.project.data.network.repositoryImpl

import kotlinx.coroutines.flow.Flow
import lee.project.data.network.datasource.OffStoreDataSource
import lee.project.data.network.retrofit.store.toDomain
import lee.project.data.network.util.safeFlow
import lee.project.domain.offstore.model.OffStoreListModel
import lee.project.domain.offstore.repository.OffStoreRepository
import javax.inject.Inject

class OffStoreRepositoryImpl @Inject constructor(
    private val offStoreDataSource: OffStoreDataSource
) : OffStoreRepository {

    override fun getOffStoreInfo(itemId: String): Flow<OffStoreListModel> = safeFlow {
        offStoreDataSource.getOffStoreInfo(
            itemId = itemId,
            itemIdType = "itemId", // 필요 시 "ISBN13" 등으로 유연하게 대처
        ).toDomain()
    }
}