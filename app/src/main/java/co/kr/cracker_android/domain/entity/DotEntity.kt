package co.kr.cracker_android.domain.entity

data class DotEntity(
    val date: String,
    val latitude: Float,
    val longitude: Float,
    val originalImage: String,
    val predictionImage: String,
    val crackRatio: Float
)
