package co.kr.cracker_android.domain.usecase

import co.kr.cracker_android.domain.entity.DotsEntity
import co.kr.cracker_android.domain.repository.DotsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DotsUseCase @Inject constructor(
    private val dotsRepository: DotsRepository,
) {
    suspend operator fun invoke(): Result<List<DotsEntity>> =
        withContext(Dispatchers.IO) {
            runCatching {
                dotsRepository.getDots()
            }
        }
}