package lee.project.domain.history.repository

import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    fun getSearchHistory(): Flow<List<String>>

    suspend fun addSearchHistory(keyword: String)
    suspend fun removeSearchHistory(keyword: String)
    suspend fun clearHistory()
}