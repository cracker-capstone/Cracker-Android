package co.kr.cracker_android.domain.usecase

import co.kr.cracker_android.domain.entity.ImageUploadEntity
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
        runCatching {
            withContext(Dispatchers.IO) {
                imageUploadRepository.uploadImage(
                    ImageUploadEntity(
                        uuid,
                        timeStamp,
                        ImageUploadEntity.Location(longitude, latitude),
                        ImageUploadEntity.Image(originalImage, predictionImage)
                    )
                )
            }
        }
}