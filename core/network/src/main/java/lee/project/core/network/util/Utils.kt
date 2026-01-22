package lee.project.core.network.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retry
import retrofit2.HttpException
import java.io.IOException

// 공통 Flow 생성 및 에러 처리를 위한 헬퍼 함수
fun <T> safeNetworkFlow(block: suspend () -> T): Flow<T> = flow {
    emit(block())
}.retry(2) { e ->
    e is IOException // 네트워크 연결 오류 시 2회 재시도
}.catch { e ->
    when (e) {
        is HttpException -> throw Exception("서버 에러: ${e.code()}")
        is IOException -> throw Exception("네트워크 연결을 확인해주세요.")
        else -> throw e
    }
}.flowOn(Dispatchers.IO)