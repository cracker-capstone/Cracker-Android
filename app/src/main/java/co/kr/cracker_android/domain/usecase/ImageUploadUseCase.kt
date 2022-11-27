package co.kr.cracker_android.domain.usecase

import co.kr.cracker_android.domain.entity.Image
import co.kr.cracker_android.domain.entity.ImageUploadEntity
import co.kr.cracker_android.domain.entity.Location
import co.kr.cracker_android.domain.repository.ImageUploadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImageUploadUseCase @Inject constructor(
    private val imageUploadRepository: ImageUploadRepository
) {
    suspend operator fun invoke(
        uuid: String,
        timeStamp: String,
        longitude: String,
        latitude: String,
        originalImage: String,
        predictionImage: String
    ): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                imageUploadRepository.uploadImage(
                    ImageUploadEntity(
                        uuid,
                        timeStamp,
                        Location(longitude, latitude),
                        Image(originalImage, predictionImage)
                    )
                )
            }
        }
}