package co.kr.cracker_android.domain.repository

import co.kr.cracker_android.domain.entity.ImageUploadEntity

interface ImageUploadRepository {
    suspend fun uploadImage(imageUploadEntity: ImageUploadEntity)
}