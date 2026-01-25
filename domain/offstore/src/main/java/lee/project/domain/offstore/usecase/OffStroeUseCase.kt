package lee.project.domain.offstore.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import lee.project.domain.offstore.model.OffStoreListModel
import lee.project.domain.offstore.repository.OffStoreRepository
import lee.project.domian.shared.LoadResult

class GetOffStoreInfoUseCase(
    private val offStoreRepository: OffStoreRepository
) {
    operator fun invoke(itemId: String): Flow<LoadResult<OffStoreListModel>> = flow {
        emit(LoadResult.Loading)
        offStoreRepository.getOffStoreInfo(itemId).collect { offStoreInfo ->
            emit(LoadResult.Success(offStoreInfo))
        }
    }.catch { e ->
        emit(LoadResult.Error(e))
    }
}