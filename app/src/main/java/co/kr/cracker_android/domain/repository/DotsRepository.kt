package co.kr.cracker_android.domain.repository

import co.kr.cracker_android.domain.entity.DotEntity
import co.kr.cracker_android.domain.entity.DotsEntity

interface DotsRepository {
    suspend fun getDots(): List<DotsEntity>
    suspend fun getDot(latitude: Float, longitude: Float): DotEntity
}