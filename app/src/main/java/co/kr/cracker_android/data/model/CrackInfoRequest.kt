package co.kr.cracker_android.data.model

import com.google.gson.annotations.SerializedName

data class CrackInfoRequest(
    @SerializedName("mac-address")
    val uuid: String,
    @SerializedName("timestamp")
    val timeStamp: String,
    val location: String,
    val images: String
)
