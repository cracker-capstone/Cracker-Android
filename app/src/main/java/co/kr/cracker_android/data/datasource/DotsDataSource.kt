package co.kr.cracker_android.data.datasource

import co.kr.cracker_android.domain.entity.DotEntity
import co.kr.cracker_android.domain.entity.DotsEntity

interface DotsDataSource {
    suspend fun getDots(): List<DotsEntity>
    suspend fun getDot(latitude: Float, longitude: Float): DotEntity
}