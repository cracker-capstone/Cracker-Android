package co.kr.cracker_android.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.cracker_android.domain.entity.DotEntity
import co.kr.cracker_android.domain.entity.DotsEntity
import co.kr.cracker_android.domain.usecase.DotUseCase
import co.kr.cracker_android.domain.usecase.DotsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DotMapViewModel @Inject constructor(
    private val dotsUseCase: DotsUseCase,
    private val dotUseCase: DotUseCase
) : ViewModel() {
    private val _dots = MutableLiveData<List<DotsEntity>>()
    val dots: LiveData<List<DotsEntity>> get() = _dots
    private val _dot = MutableLiveData<DotEntity>()
    val dot: LiveData<DotEntity> get() = _dot

    fun getDots() {
        viewModelScope.launch {
            dotsUseCase()
                .onSuccess { _dots.value = it }
                .onFailure { Timber.e(it) }
        }
    }

    fun getDot(latitude: Float, longitude: Float) {
        viewModelScope.launch {
            dotUseCase(latitude, longitude)
                .onSuccess { _dot.value = it }
                .onFailure { Timber.e(it) }
        }
    }
}