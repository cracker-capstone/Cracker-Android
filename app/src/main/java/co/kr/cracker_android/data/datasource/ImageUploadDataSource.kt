package co.kr.cracker_android.data.datasource

import co.kr.cracker_android.domain.entity.ImageUploadEntity

interface ImageUploadDataSource {
    suspend fun uploadCrackInfo(
        imageUploadEntity: ImageUploadEntity
    )
}