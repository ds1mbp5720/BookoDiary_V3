package lee.project.core.network.error

sealed class NetworkError : Exception() {
    data class BadRequest(val msg: String) : NetworkError()
    data class Unauthorized(val code: String, val msg: String) : NetworkError()
    object Timeout : NetworkError()
    object Unknown : NetworkError()
}