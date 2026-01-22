package lee.project.data.offstore.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lee.project.data.offstore.remote.OffStoreApi
import lee.project.data.offstore.remote.datasource.OffStoreDataSource
import lee.project.data.offstore.repository_impl.OffStoreRepositoryImpl
import lee.project.domain.offstore.repository.OffStoreRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OffStoreCoreModule {
    @Provides
    @Singleton
    fun provideOffStoreDataSource(
        offStoreApi: OffStoreApi
    ): OffStoreDataSource {
        return OffStoreDataSource(offStoreApi)
    }

    @Provides
    @Singleton
    fun provideOffStoreApi(retrofit: Retrofit): OffStoreApi {
        return retrofit.create(OffStoreApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class OffStoreModule {

    @Binds
    @Singleton
    abstract fun bindBookListRepository(
        restaurantRepositoryImpl: OffStoreRepositoryImpl
    ): OffStoreRepository
}