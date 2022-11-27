package co.kr.cracker_android.data.model

data class BaseResponse<T>(
    val status: Int,
    val message: String,
    val result: T
)