package lee.project.core.network.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retry
import lee.project.core.network.error.ErrorMapper
import lee.project.core.network.error.NetworkError
import java.io.IOException

// 공통 Flow 생성 및 에러 처리를 위한 헬퍼 함수
fun <T> safeNetworkFlow(
    errorMapper: ErrorMapper, block: suspend () -> T
): Flow<T> = flow {
    emit(block())
}.retry(2) { e ->
    e is IOException
}.catch { e ->
    if (e is NetworkError) {
        throw e
    } else {
        throw errorMapper.map(e)
    }
}.flowOn(Dispatchers.IO)