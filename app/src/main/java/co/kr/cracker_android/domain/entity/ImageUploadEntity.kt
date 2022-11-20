package co.kr.cracker_android.domain.entity

data class ImageUploadEntity(
    val uuid: String,
    val timeStamp: String,
    val location: Location,
    val images: Image
) {
    data class Location(
        val longitude: String,
        val latitude: String
    ) {
        override fun toString(): String {
            return "{\"longitude\":\"$longitude\", \"latitude\":\"$latitude\"}"
        }
    }

    data class Image(
        val originalImage: String,
        val predictionImage: String
    ) {
        override fun toString(): String {
            return "{\"true\":\"$originalImage\", \"pred\":\"$predictionImage\"}"
        }
    }
}