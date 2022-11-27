package co.kr.cracker_android.domain.usecase

import co.kr.cracker_android.domain.entity.DotEntity
import co.kr.cracker_android.domain.repository.DotsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DotUseCase @Inject constructor(
    private val dotsRepository: DotsRepository
) {
    suspend operator fun invoke(latitude: Float, longitude: Float): Result<DotEntity> {
        return withContext(Dispatchers.IO) {
            runCatching {
                dotsRepository.getDot(latitude, longitude)
            }
        }
    }
}