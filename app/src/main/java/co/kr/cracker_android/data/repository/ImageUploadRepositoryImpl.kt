package co.kr.cracker_android.data.repository

import co.kr.cracker_android.data.datasource.ImageUploadDataSource
import co.kr.cracker_android.domain.entity.ImageUploadEntity
import co.kr.cracker_android.domain.repository.ImageUploadRepository
import javax.inject.Inject

class ImageUploadRepositoryImpl @Inject constructor(
    private val imageUploadDataSource: ImageUploadDataSource
) : ImageUploadRepository {
    override suspend fun uploadImage(imageUploadEntity: ImageUploadEntity) {
        imageUploadDataSource.uploadCrackInfo(imageUploadEntity)
    }
}