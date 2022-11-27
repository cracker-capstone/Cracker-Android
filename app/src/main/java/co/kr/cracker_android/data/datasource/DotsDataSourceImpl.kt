package co.kr.cracker_android.data.datasource

import co.kr.cracker_android.data.model.DotResponse
import co.kr.cracker_android.data.model.DotsInfoResponse
import co.kr.cracker_android.data.service.DotsService
import co.kr.cracker_android.domain.entity.DotEntity
import co.kr.cracker_android.domain.entity.DotsEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class DotsDataSourceImpl @Inject constructor(
    private val dotsService: DotsService,
    private val gson: Gson
) : DotsDataSource {
    override suspend fun getDots(): List<DotsEntity> {
        return gson.fromJson<List<DotsInfoResponse>>(
            dotsService.getDots().result,
            object : TypeToken<List<DotsInfoResponse>>() {}.type
        ).map { it.toEntity() }
    }

    override suspend fun getDot(latitude: Float, longitude: Float): DotEntity {
        return gson.fromJson(
            dotsService.getDot(latitude, longitude).result,
            DotResponse::class.java
        ).toEntity()
    }
}