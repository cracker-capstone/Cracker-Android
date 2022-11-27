package co.kr.cracker_android.domain.entity

data class Image(
    val originalImage: String,
    val predictionImage: String
) {
    override fun toString(): String {
        return "{\"true\":\"$originalImage\", \"pred\":\"$predictionImage\"}"
    }
}
