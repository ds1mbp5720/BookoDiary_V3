package lee.project.domain.book.model

data class BookListModel(
    val baseModel: BaseModel,
    val bookList: List<BookModel>
)

data class BookModel(
    val itemId: String?,
    val title: String?,
    val link: String?,
    val author: String?,
    val pubDate: String?,
    val description: String?,
    val isbn: String?,
    val isbn13: String?,
    val priceSales: String?,
    val priceStandard: String?,
    val mallType: String?,
    val stockStatus: String?,
    val mileage: String?,
    val cover: String?,
    val categoryId: String?,
    val categoryName: String?,
    val publisher: String?,
    val salesPoint: String?,
    val adult: Boolean?,
    val fixedPrice: Boolean?,
    val customerReviewRank: String?,
    val subInfo: SubInfoModel?
)


data class SubInfoModel(
    val subTitle: String?, //부제
    val originalTitle: String?, //원제
    val itemPage: String?, //상품 쪽수
    val subbarcode: String?, //부가기호
    val cardReviewImgList: List<String>, //카드리뷰 이미지 경로
    val ratingInfo: RatingInfoModel?, //상품의 리뷰, 평점관련 개수
    val bestSellerRank: String?, //베스트셀러 순위
)

data class RatingInfoModel(
    val ratingScore: String?,
    val ratingCount: String?,
    val commentReviewCount: String?,
    val myReviewCount: String?,
)
