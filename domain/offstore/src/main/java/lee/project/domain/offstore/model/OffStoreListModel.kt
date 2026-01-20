package lee.project.domain.offstore.model

data class OffStoreListModel(
    val link: String?,
    val pubDate: String?,
    val query: String?,
    val version: String?,
    val itemOffStoreList: List<OffStoreModel>?
)


data class OffStoreModel(
    val offCode: String?,
    val offName: String?,
    val link: String?
)