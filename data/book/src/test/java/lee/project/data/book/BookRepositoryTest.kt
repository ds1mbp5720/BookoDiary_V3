package lee.project.data.book

import app.cash.turbine.test
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import lee.project.core.network.error.ErrorMapper
import lee.project.core.network.error.NetworkError
import lee.project.data.book.remote.datasource.BookListDataSource
import lee.project.data.book.remote.dto.AladinResponse
import lee.project.data.book.remote.dto.BookDto
import lee.project.data.book.repository_impl.BookListRepositoryImpl
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class BookRepositoryTest {

    // 1. Mock 객체 생성
    private val dataSource: BookListDataSource = mockk()
    private val errorMapper: ErrorMapper = ErrorMapper(Moshi.Builder().add(KotlinJsonAdapterFactory()).build())

    private lateinit var repository: BookListRepositoryImpl

    @Before
    fun setUp() {
        repository = BookListRepositoryImpl(dataSource, errorMapper)
    }

    @Test
    fun `getBookList 성공 시 도메인 모델을 반환한다`() = runTest {
        // Given: 정상적인 DTO 응답 설정
        val mockResponse = AladinResponse(
            items = listOf(BookDto(itemId = "1",isbn = "123", title = "테스트 책")),
            errorCode = null
        )
        coEvery { dataSource.getBookList(any(), any()) } returns mockResponse

        repository.getBookList("BestSeller", 1).test {
            val result = awaitItem()
            assert(result.bookList[0].title == "테스트 책")
            awaitComplete()
        }
    }

    @Test
    fun `알라딘 비즈니스 에러 발생 시 NetworkError_BadRequest를 던진다`() = runTest {
        // Given: errorCode가 포함된 응답 설정
        val errorResponse = AladinResponse<BookDto>(
            errorCode = 8,
            errorMessage = "검색어가 너무 짧습니다."
        )
        coEvery { dataSource.searchBookList(any(), any()) } returns errorResponse

        repository.searchBookList("가").test {
            val error = awaitError()
            assert(error is NetworkError.BadRequest)
            assert((error as NetworkError.BadRequest).msg == "검색어가 너무 짧습니다.")
        }
    }

    @Test
    fun `네트워크 타임아웃 발생 시 NetworkError_Timeout을 던진다`() = runTest {
        // Given: IOException 발생 시뮬레이션
        coEvery { dataSource.getBookDetail(any()) } throws IOException()

        repository.getBookDetail(123L).test {
            val error = awaitError()
            assert(error is NetworkError.Timeout)
        }
    }
}