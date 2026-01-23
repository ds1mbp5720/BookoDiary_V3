package lee.project.data.book.di

import android.content.Context
import androidx.room.RoomDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import lee.project.core.database.room.uitl.DatabaseUtil
import lee.project.data.book.local.dao.BookDao
import lee.project.data.book.local.database.BookDatabase
import lee.project.data.book.remote.BookListApi
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

    @Provides
    @Singleton
    fun provideBookDatabase(@ApplicationContext context: Context): BookDatabase {
        return DatabaseUtil.createDatabase(
            context = context,
            dbClass = BookDatabase::class.java,
            dbName = "aladin_book.db"
        )
    }

    @Provides
    @Singleton
    fun provideBookDao(database: BookDatabase): BookDao {
        return database.bookDao()
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