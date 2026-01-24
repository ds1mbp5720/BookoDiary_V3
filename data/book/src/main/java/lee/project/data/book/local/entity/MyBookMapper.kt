package lee.project.data.book.local.entity

import lee.project.domain.book.model.MyBookModel

fun MyBookEntity.toDomain(): MyBookModel = MyBookModel(
    itemId = itemId,
    imageUrl = imageUrl,
    title = title,
    author = author,
    link = link,
    myReview = myReview,
    period = period,
    rating = rating
)

fun MyBookModel.toEntity(): MyBookEntity = MyBookEntity(
    itemId = itemId,
    imageUrl = imageUrl,
    title = title,
    author = author,
    link = link,
    myReview = myReview,
    period = period,
    rating = rating
)