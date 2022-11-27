package co.kr.cracker_android.data.model

import co.kr.cracker_android.domain.entity.DotsEntity
import com.google.gson.annotations.SerializedName

data class DotsInfoResponse(
    val date: String,
    val latitude: Float,
    val longitude: Float,
    @SerializedName("crack_ratio")
    val crackRatio: Float
) {
    fun toEntity(): DotsEntity {
        return DotsEntity(
            this.date,
            this.latitude,
            this.longitude,
            this.crackRatio
        )
    }
}