package co.kr.cracker_android.data.model

import co.kr.cracker_android.domain.entity.DotEntity
import com.google.gson.annotations.SerializedName

data class DotResponse(
    val date: String,
    val latitude: Float,
    val longitude: Float,
    @SerializedName("true")
    val originalImage: String,
    @SerializedName("pred")
    val predictionImage: String,
    @SerializedName("crack_ratio")
    val crackRatio: Float
) {
    fun toEntity(): DotEntity {
        return DotEntity(
            this.date,
            this.latitude,
            this.longitude,
            this.originalImage,
            this.predictionImage,
            this.crackRatio
        )
    }
}
