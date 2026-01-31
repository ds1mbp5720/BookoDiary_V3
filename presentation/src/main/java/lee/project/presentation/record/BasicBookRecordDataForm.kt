package lee.project.presentation.record

import lee.project.domain.book.model.MyBookModel
import lee.project.domain.book.model.WishBookModel

data class BasicBookRecord(
    val itemId: Long,
    val imageUrl: String,
    val title: String
)

/**
 * record compose 에서 사용하는 공통 양식 부분 추출목적
 * compose 함수의 재사용성을 위한 가공부분
 */
fun toBasicBookRecord(data: Any): BasicBookRecord? {
    return when (data) {
        is MyBookModel -> {
            BasicBookRecord(
                itemId = data.itemId,
                imageUrl = data.imageUrl,
                title = data.title
            )
        }

        is WishBookModel -> {
            BasicBookRecord(
                itemId = data.itemId,
                imageUrl = data.imageUrl,
                title = data.title
            )
        }

        else -> {
            null
        }
    }

}

fun MyBookModel.mapperMyBookToBasicBook(): BasicBookRecord? {
    return toBasicBookRecord(this)
}

fun List<MyBookModel>.mapperMyBookToBasicBookRecordList(): List<BasicBookRecord> {
    val basicBookRecordList = mutableListOf<BasicBookRecord>()
    this.forEach {
        it.mapperMyBookToBasicBook()?.let { it1 -> basicBookRecordList.add(it1) }
    }
    return basicBookRecordList
}

fun WishBookModel.mapperWishBookToBasicBookRecord(): BasicBookRecord? {
    return toBasicBookRecord(this)
}

fun List<WishBookModel>.mapperWishBookToBasicBookRecordList(): List<BasicBookRecord> {
    val basicBookRecordList = mutableListOf<BasicBookRecord>()
    this.forEach {
        it.mapperWishBookToBasicBookRecord()?.let { it1 -> basicBookRecordList.add(it1) }
    }
    return basicBookRecordList
}
