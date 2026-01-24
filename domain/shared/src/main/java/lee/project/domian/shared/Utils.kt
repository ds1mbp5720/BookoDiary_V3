package lee.project.domian.shared

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed class LoadResult<out R> {
    data class Success<out T>(val data: T) : LoadResult<T>()
    data class Error(val exception: Throwable) : LoadResult<Nothing>()
    object Loading : LoadResult<Nothing>()
}

fun <T> Flow<T>.asResult(): Flow<LoadResult<T>> {
    return this
        .map<T, LoadResult<T>> { LoadResult.Success(it) }
        .onStart { emit(LoadResult.Loading) }
        .catch { emit(LoadResult.Error(it)) }
}