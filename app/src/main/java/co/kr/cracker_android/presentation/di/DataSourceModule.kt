package co.kr.cracker_android.presentation.di

import co.kr.cracker_android.data.datasource.ImageUploadDataSource
import co.kr.cracker_android.data.datasource.ImageUploadDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindImageUploadDataSource(imageUploadDataSourceImpl: ImageUploadDataSourceImpl): ImageUploadDataSource
}