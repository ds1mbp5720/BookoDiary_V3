package lee.project.domain.offstore.repository

import kotlinx.coroutines.flow.Flow
import lee.project.domain.offstore.model.OffStoreListModel

interface OffStoreRepository {
    fun getOffStoreInfo(itemId: String): Flow<OffStoreListModel>
}