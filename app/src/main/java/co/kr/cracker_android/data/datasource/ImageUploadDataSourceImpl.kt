package co.kr.cracker_android.data.datasource

import co.kr.cracker_android.data.model.CrackInfoRequest
import co.kr.cracker_android.data.service.ImageUploadService
import co.kr.cracker_android.domain.entity.ImageUploadEntity
import javax.inject.Inject

class ImageUploadDataSourceImpl @Inject constructor(
    private val imageUploadService: ImageUploadService
) : ImageUploadDataSource {
    override suspend fun uploadCrackInfo(
        imageUploadEntity: ImageUploadEntity
    ) {
        imageUploadService.uploadCrackInfo(
            CrackInfoRequest(
                imageUploadEntity.uuid,
                imageUploadEntity.timeStamp,
                imageUploadEntity.location.toString(),
                imageUploadEntity.images.toString()
            )
        )
    }
}