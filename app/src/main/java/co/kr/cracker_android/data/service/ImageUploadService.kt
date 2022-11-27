package co.kr.cracker_android.data.service

import co.kr.cracker_android.data.model.CrackInfoRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface ImageUploadService {
    @POST("/api/crack-info-store/upload")
    suspend fun uploadCrackInfo(
        @Body body: CrackInfoRequest
    )
}