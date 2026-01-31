package lee.project.data.book.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// 찜한 책 (단순 저장)
@Entity(tableName = "wish_books")
data class WishBookEntity(
    @PrimaryKey val itemId: Long,
    val title: String,
    val imageUrl: String, // 표지 이미지
    val addedAt: Long = System.currentTimeMillis()
)