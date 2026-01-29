package lee.project.data.history.repository_impl

import android.content.Context
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import lee.project.domain.history.repository.SearchHistoryRepository
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "search_history")

class SearchHistoryRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SearchHistoryRepository {

    private val historyKey = stringSetPreferencesKey("history_list")

    override fun getSearchHistory(): Flow<List<String>> {
        return context.dataStore.data.map { preferences ->
            preferences[historyKey]?.toList() ?: emptyList()
        }
    }

    override suspend fun addSearchHistory(keyword: String) {
        context.dataStore.edit { preferences ->
            val current = preferences[historyKey] ?: emptySet()
            // 새로운 검색어를 맨 앞으로 (LinkedHashSet 활용 시) 또는 단순 추가 후 정렬
            preferences[historyKey] = (setOf(keyword) + current).take(10).toSet() // 최근 10개 유지
        }
    }

    override suspend fun removeSearchHistory(keyword: String) {
        context.dataStore.edit { preferences ->
            val current = preferences[historyKey] ?: emptySet()
            preferences[historyKey] = current.filter { it != keyword }.toSet()
        }
    }

    override suspend fun clearHistory() {
        context.dataStore.edit { it.remove(historyKey) }
    }
}