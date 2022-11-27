package co.kr.cracker_android.domain.entity

data class ImageUploadEntity(
    val uuid: String,
    val timeStamp: String,
    val location: Location,
    val images: Image
)