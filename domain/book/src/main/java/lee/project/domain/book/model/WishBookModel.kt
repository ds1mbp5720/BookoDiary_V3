package lee.project.domain.book.model

data class WishBookModel(
    val itemId: String,
    val imageUrl: String, // 표지 이미지
    val title: String, // 상품명
    val addedAt: Long // 찜 시간
)
