package lee.project.domain.history.usecase

import kotlinx.coroutines.flow.Flow
import lee.project.domain.history.repository.SearchHistoryRepository
import javax.inject.Inject

class GetSearchHistoryUseCase @Inject constructor(
    private val repository: SearchHistoryRepository
) {
    operator fun invoke(): Flow<List<String>> = repository.getSearchHistory()
}

class AddSearchHistoryUseCase @Inject constructor(
    private val repository: SearchHistoryRepository
) {
    suspend operator fun invoke(keyword: String) = repository.addSearchHistory(keyword)
}

class RemoveSearchHistoryUseCase @Inject constructor(
    private val repository: SearchHistoryRepository
) {
    suspend operator fun invoke(keyword: String) = repository.removeSearchHistory(keyword)
}

class ClearSearchHistoryUseCase @Inject constructor(
    private val repository: SearchHistoryRepository
) {
    suspend operator fun invoke() = repository.clearHistory()
}
