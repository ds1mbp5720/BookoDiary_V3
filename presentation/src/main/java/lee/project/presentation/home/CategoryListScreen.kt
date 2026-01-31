package lee.project.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.flowOf
import lee.project.domain.book.model.BookModel
import lee.project.presentation.R
import lee.project.presentation.book.BookItemList
import lee.project.presentation.component.BasicUpButton
import lee.project.presentation.component.BookDiaryDivider
import lee.project.presentation.theme.BookDiaryTheme

@Composable
fun SingleCategoryListScreen(
    modifier: Modifier = Modifier,
    listType: HomeListType,
    onBookClick: (Long) -> Unit,
    upPress: () -> Unit,
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {
    val books = viewModel.singleCategoryBookList.collectAsLazyPagingItems()

    SingleCategoryListContent(
        modifier = modifier,
        listType = listType,
        books = books,
        onBookClick = onBookClick,
        upPress = upPress
    )
}

@Composable
private fun SingleCategoryListContent(
    modifier: Modifier = Modifier,
    listType: HomeListType,
    books: LazyPagingItems<BookModel>,
    onBookClick: (Long) -> Unit,
    upPress: () -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicUpButton(upPress, padding = 2.dp)
            Text(
                text = stringResource(
                    id = when (listType) {
                        HomeListType.ItemNewAll -> R.string.str_new_all_title
                        HomeListType.ItemNewSpecial -> R.string.str_new_special_title
                        HomeListType.Bestseller -> R.string.str_bestseller_rank
                        HomeListType.BlogBest -> R.string.str_bolg_best_title
                    }
                ),
                style = MaterialTheme.typography.headlineSmall,
                color = BookDiaryTheme.colors.brand,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        BookDiaryDivider()
        LazyColumn(contentPadding = PaddingValues(vertical = 8.dp)) {
            items(books.itemCount) { index ->
                BookItemList(
                    book = books[index],
                    onBookClick = onBookClick,
                    showDivider = index != 0
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SingleCategoryListPreview() {
    BookDiaryTheme {
        val books = flowOf(PagingData.from(listOf(
            BookModel(
                itemId = 123,
                title = "테스트 책 제목",
                author = "테스트 작가",
                cover = "",
                link = null,
                pubDate = null,
                description = null,
                isbn = null,
                isbn13 = null,
                priceSales = "12000",
                priceStandard = null,
                mallType = null,
                stockStatus = null,
                mileage = null,
                categoryId = null,
                categoryName = null,
                publisher = null,
                salesPoint = null,
                adult = null,
                fixedPrice = null,
                customerReviewRank = null,
                subInfo = null
            )
        ))).collectAsLazyPagingItems()

        SingleCategoryListContent(
            listType = HomeListType.Bestseller,
            books = books,
            onBookClick = {},
            upPress = {}
        )
    }
}
