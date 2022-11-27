package co.kr.cracker_android.data.service

import co.kr.cracker_android.data.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DotsService {
    @GET("/api/crack-info-down/download")
    suspend fun getDots(
        @Query("top_left_lat") topLeftLatitude: Float = 38.5F,
        @Query("top_left_lon") topLeftLongitude: Float = 125.1F,
        @Query("bottom_right_lat") bottomRightLatitude: Float = 33.1F,
        @Query("bottom_right_lon") bottomRightLongitude: Float = 131.9F
    ): BaseResponse<String>

    @GET("/api/crack-info-down/description")
    suspend fun getDot(
        @Query("lat") latitude: Float,
        @Query("lon") longitude: Float
    ): BaseResponse<String>
}