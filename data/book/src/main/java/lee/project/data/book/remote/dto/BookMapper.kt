package lee.project.data.book.remote.dto

import lee.project.domain.book.model.BaseModel
import lee.project.domain.book.model.BookListModel
import lee.project.domain.book.model.BookModel
import lee.project.domain.book.model.RatingInfoModel
import lee.project.domain.book.model.SubInfoModel

fun AladinResponse<BookData>.toDomain(): BookListModel {
    return BookListModel(
        baseModel = BaseModel(
            title = this.title,
            link = this.link,
            logo = this.logo,
            pubDate = this.pubDate,
            totalResults = this.totalResults.toString(),
            startIndex = this.startIndex.toString(),
            itemsPerPage = this.itemsPerPage.toString(),
            query = this.query,
            version = this.version,
            searchCategoryId = this.searchCategoryId?.toString() ?: "",
            searchCategoryName = this.searchCategoryName
        ),
        bookList = this.items.map { it.toDomain() }
    )
}

fun BookData.toDomain(): BookModel {
    return BookModel(
        itemId = this.itemId,
        title = this.title,
        link = this.link,
        author = this.author,
        pubDate = this.pubDate,
        description = this.description,
        isbn = this.isbn,
        isbn13 = this.isbn13,
        priceSales = this.priceSales,
        priceStandard = this.priceStandard,
        mallType = this.mallType,
        stockStatus = this.stockStatus,
        mileage = this.mileage,
        cover = this.cover,
        categoryId = this.categoryId,
        categoryName = this.categoryName,
        publisher = this.publisher,
        salesPoint = this.salesPoint,
        adult = this.adult,
        fixedPrice = this.fixedPrice,
        customerReviewRank = this.customerReviewRank,
        subInfo = this.subInfo?.toDomain()
    )
}

fun SubInfo.toDomain(): SubInfoModel {
    return SubInfoModel(
        subTitle = this.subTitle,
        originalTitle = this.originalTitle,
        itemPage = this.itemPage,
        subbarcode = this.subbarcode,
        cardReviewImgList = this.cardReviewImgList,
        ratingInfo = this.ratingInfo?.toDomain(),
        bestSellerRank = this.bestSellerRank
    )
}

fun RatingInfo.toDomain(): RatingInfoModel {
    return RatingInfoModel(
        ratingScore = this.ratingScore,
        ratingCount = this.ratingCount,
        commentReviewCount = this.commentReviewCount,
        myReviewCount = this.myReviewCount
    )
}