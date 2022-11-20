package co.kr.cracker_android.presentation.di

import co.kr.cracker_android.data.repository.ImageUploadRepositoryImpl
import co.kr.cracker_android.domain.repository.ImageUploadRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsImageUploadRepository(imageUploadRepositoryImpl: ImageUploadRepositoryImpl): ImageUploadRepository
}