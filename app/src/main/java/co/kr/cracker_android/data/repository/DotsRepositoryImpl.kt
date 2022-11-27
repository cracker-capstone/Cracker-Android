package co.kr.cracker_android.data.repository

import co.kr.cracker_android.data.datasource.DotsDataSource
import co.kr.cracker_android.domain.entity.DotEntity
import co.kr.cracker_android.domain.entity.DotsEntity
import co.kr.cracker_android.domain.repository.DotsRepository
import javax.inject.Inject

class DotsRepositoryImpl @Inject constructor(
    private val dotsDataSource: DotsDataSource
) : DotsRepository {
    override suspend fun getDots(): List<DotsEntity> {
        return dotsDataSource.getDots()
    }

    override suspend fun getDot(latitude: Float, longitude: Float): DotEntity {
        return dotsDataSource.getDot(latitude, longitude)
    }
}