package lee.project.domain.book.model

data class BaseModel(
    val title: String? = "",
    val link: String? = "",
    val logo: String? = "",
    val pubDate: String? = "",
    val totalResults: String? = "",
    val startIndex: String? = "",
    val itemsPerPage: String? = "",
    val query: String? = "",
    val version: String? = "",
    val searchCategoryId: String? = "",
    val searchCategoryName: String? = ""
)
