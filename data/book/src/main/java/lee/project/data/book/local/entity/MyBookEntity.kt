package lee.project.data.book.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// 내 책 (읽기 상태 등 추가 정보 가능)
@Entity(tableName = "my_books")
data class MyBookEntity(
    @PrimaryKey val itemId: Long,
    val imageUrl: String, // 표지 이미지
    val title: String, // 상품명
    val author: String, // 저자
    val link: String?, // 상품 링크 Url
    val myReview: String?, // 독후감
    val period: String, // 독서기간
    val rating: Float // 평점
)