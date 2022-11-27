package co.kr.cracker_android.presentation.di

import co.kr.cracker_android.data.service.DotsService
import co.kr.cracker_android.data.service.ImageUploadService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideImageUploadService(retrofit: Retrofit): ImageUploadService =
        retrofit.create(ImageUploadService::class.java)

    @Provides
    @Singleton
    fun provideDotsService(retrofit: Retrofit): DotsService =
        retrofit.create(DotsService::class.java)
}