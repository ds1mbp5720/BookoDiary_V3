package lee.project.data.network.retrofit.store

import lee.project.domain.offstore.model.OffStoreListModel
import lee.project.domain.offstore.model.OffStoreModel

fun OffStoreData.toDomain(): OffStoreModel {
    return OffStoreModel(
        offCode = this.offCode,
        offName = this.offName,
        link = this.link
    )
}

fun OffStoreListData.toDomain(): OffStoreListModel {
    return OffStoreListModel(
        link = this.link,
        pubDate = this.pubDate,
        query = this.query,
        version = this.version,
        itemOffStoreList = this.itemOffStoreList.map { it.toDomain() }
    )
}