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
import lee.project.domain.book.model.WishBookModel
import lee.project.domain.book.repository.BookLibraryRepository
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WishBookTest {
    private lateinit var database: BookDatabase
    private lateinit var bookDao: BookDao
    private lateinit var repository: BookLibraryRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // 메모리 내 임시 DB 생성
        database = Room.inMemoryDatabaseBuilder(context, BookDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        bookDao = database.bookDao()
        repository = BookLibraryRepositoryImpl(bookDao)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetWishBookTest() = runTest {
        // Given: 테스트 데이터 준비
        val wishBook = WishBookModel(
            itemId = "wish_1",
            title = "테스트 찜 도서",
            imageUrl = "url",
            addedAt = System.currentTimeMillis()
        )

        // When: 저장
        repository.insertWishBook(wishBook)

        // Then: 전체 목록 조회 및 검증 (Turbine 활용)
        repository.getAllWishBooks().test {
            val list = awaitItem()
            assertEquals(1, list.size)
            assertEquals("테스트 찜 도서", list[0].title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteWishBookTest() = runTest {
        // Given: 데이터 저장 후
        val wishBook = WishBookModel("id_1", "url", "제목", 0L)
        repository.insertWishBook(wishBook)

        // When: 삭제
        repository.deleteWishBook("id_1")

        // Then: 목록이 비어있어야 함
        repository.getAllWishBooks().test {
            assertEquals(0, awaitItem().size)
            cancelAndIgnoreRemainingEvents()
        }
    }
}