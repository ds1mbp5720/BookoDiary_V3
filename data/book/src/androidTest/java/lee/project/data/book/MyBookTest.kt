package lee.project.data.book

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import lee.project.data.book.local.dao.BookDao
import lee.project.data.book.local.database.BookDatabase
import lee.project.data.book.repository_impl.BookLibraryRepositoryImpl
import lee.project.domain.book.model.MyBookModel
import lee.project.domain.book.repository.BookLibraryRepository
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MyBookTest {
    private lateinit var database: BookDatabase
    private lateinit var bookDao: BookDao
    private lateinit var repository: BookLibraryRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, BookDatabase::class.java).build()
        bookDao = database.bookDao()
        repository = BookLibraryRepositoryImpl(bookDao)
    }

    @After
    fun tearDown() = database.close()

    @Test
    fun insertAndSearchMyBookTest() = runTest {
        // Given: 독후감이 포함된 내 서재 데이터
        val myBook = MyBookModel(
            itemId = "my_1",
            imageUrl = "url",
            title = "코틀린 정복",
            author = "개발자",
            link = "link",
            myReview = "정말 유익한 책이었다.",
            period = "2024.01~2024.02",
            rating = 5
        )
        repository.insertMyBook(myBook)

        // When: '코틀린'으로 검색
        repository.searchMyBooks("코틀린").test {
            // Then
            val results = awaitItem()
            assertEquals(1, results.size)
            assertEquals("코틀린 정복", results[0].title)
            assertEquals(5, results[0].rating)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateMyBookTest() = runTest {
        // Given: 초기 데이터 삽입 (같은 itemId)
        val initialBook = MyBookModel("id", "url", "제목", "저자", null, "구린 리뷰", "기간", 1)
        repository.insertMyBook(initialBook)

        // When: 같은 ID로 평점과 리뷰를 바꿔서 삽입 (OnConflictStrategy.REPLACE 작동)
        val updatedBook = initialBook.copy(myReview = "좋은 리뷰", rating = 5)
        repository.insertMyBook(updatedBook)

        // Then: 최종 데이터 검증
        repository.getAllMyBooks().test {
            val item = awaitItem()[0]
            assertEquals("좋은 리뷰", item.myReview)
            assertEquals(5, item.rating)
            cancelAndIgnoreRemainingEvents()
        }
    }
}