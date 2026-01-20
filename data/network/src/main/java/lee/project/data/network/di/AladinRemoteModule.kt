package lee.project.data.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lee.project.data.network.datasource.BookListDataSource
import lee.project.data.network.datasource.OffStoreDataSource
import lee.project.data.network.retrofit.book.BookListApi
import lee.project.data.network.retrofit.store.OffStoreApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AladinRemoteModule {

    @Provides
    @Singleton
    fun provideBookListDatasource(
        bookListApi: BookListApi
    ): BookListDataSource {
        return BookListDataSource(bookListApi)
    }

    @Provides
    @Singleton
    fun provideOffStoreDataSource(
        offStoreApi: OffStoreApi
    ): OffStoreDataSource {
        return OffStoreDataSource(offStoreApi)
    }
}