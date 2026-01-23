package lee.project.core.network.error

import com.squareup.moshi.Moshi
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorMapper @Inject constructor(build: Moshi) {
    fun map(throwable: Throwable): NetworkError {
        return when (throwable) {
            is HttpException -> when (throwable.code()) {
                401 -> NetworkError.Unauthorized("HTTP_401", "인증 세션이 만료되었습니다.")
                else -> NetworkError.BadRequest("서버 응답 오류 (${throwable.code()})")
            }
            is IOException -> NetworkError.Timeout
            else -> NetworkError.Unknown
        }
    }
}