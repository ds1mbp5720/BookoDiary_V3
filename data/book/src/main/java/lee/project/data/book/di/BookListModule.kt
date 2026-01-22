package lee.project.data.book.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lee.project.data.book.remote.book.BookListApi
import lee.project.data.book.remote.datasource.BookListDataSource
import lee.project.data.book.repository_impl.BookListRepositoryImpl
import lee.project.domain.book.repository.BookListRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BookCoreModule {

    @Provides
    @Singleton
    fun provideBookListApi(retrofit: Retrofit): BookListApi {
        return retrofit.create(BookListApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBookListDatasource(
        bookListApi: BookListApi
    ): BookListDataSource {
        return BookListDataSource(bookListApi)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class BookListModule {

    @Binds
    @Singleton
    abstract fun bindBookListRepository(
        restaurantRepositoryImpl: BookListRepositoryImpl
    ): BookListRepository
}