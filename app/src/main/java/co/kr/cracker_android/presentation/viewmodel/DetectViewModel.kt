package co.kr.cracker_android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.cracker_android.domain.usecase.ImageUploadUseCase
import co.kr.cracker_android.util.toDateString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetectViewModel @Inject constructor(
    private val imageUploadUseCase: ImageUploadUseCase
) : ViewModel() {
    fun uploadCrackInfo(
        uuid: String,
        timeStamp: Long,
        longitude: String,
        latitude: String,
        originalImage: String,
        predictionImage: String
    ) {
        viewModelScope.launch {
            imageUploadUseCase(
                uuid,
                timeStamp.toDateString(),
                longitude,
                latitude,
                originalImage,
                predictionImage
            )
                .onSuccess { Timber.tag("postData").i("success") }
                .onFailure {
                    Timber.tag("postData").e("failed")
                    Timber.tag("postData").e(it)
                }
        }
    }
}