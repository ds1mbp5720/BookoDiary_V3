package lee.project.data.book.local.entity

import lee.project.domain.book.model.WishBookModel

fun WishBookEntity.toDomain(): WishBookModel = WishBookModel(
    itemId = itemId,
    title = title,
    imageUrl = imageUrl,
    addedAt = addedAt
)

fun WishBookModel.toEntity(): WishBookEntity = WishBookEntity(
    itemId = itemId,
    title = title,
    imageUrl = imageUrl,
    addedAt = addedAt
)