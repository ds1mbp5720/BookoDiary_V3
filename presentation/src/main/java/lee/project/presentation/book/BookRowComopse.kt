package lee.project.presentation.book

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import lee.project.domain.book.model.BookModel
import lee.project.presentation.R
import lee.project.presentation.component.BookCard
import lee.project.presentation.component.BookImageCard
import lee.project.presentation.component.offsetGradientBackground
import lee.project.presentation.home.HomeListType
import lee.project.presentation.theme.BookDiaryTheme
import lee.project.presentation.util.mirroringIcon

private val cardWidth = 170.dp
private val cardPadding = 16.dp

/**
 * 가로형 책 list
 */
@Composable
fun BookRowContent(
    contentTitle: HomeListType,
    books: LazyPagingItems<BookModel>,
    onBookClick: (Long) -> Unit,
    onListClick: (String) -> Unit,
    viewModel: HomeViewModel = viewModel(),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    index: Int = 0
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .heightIn(min = 56.dp)
                .padding(start = 24.dp)
        ) {
            Text(
                text = stringResource(
                    id = when (contentTitle) {
                        HomeListType.ItemNewAll -> {
                            R.string.str_new_all_title
                        }

                        HomeListType.ItemNewSpecial -> {
                            R.string.str_new_special_title
                        }

                        HomeListType.Bestseller -> {
                            R.string.str_bestseller_rank
                        }

                        HomeListType.BlogBest -> {
                            R.string.str_bolg_best_title
                        }
                    }
                ),
                style = MaterialTheme.typography.titleLarge,
                color = BookDiaryTheme.colors.textPrimary,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
            )
            IconButton(
                onClick = {
                    when (contentTitle) {
                        HomeListType.ItemNewAll -> {
                            viewModel.getSingleCategoryBookList("ItemNewAll", 100)
                            onListClick(HomeListType.ItemNewAll.toString())
                        }

                        HomeListType.ItemNewSpecial -> {
                            viewModel.getSingleCategoryBookList("ItemNewSpecial", 100)
                            onListClick(HomeListType.ItemNewSpecial.toString())
                        }

                        HomeListType.Bestseller -> {
                            viewModel.getSingleCategoryBookList("Bestseller", 100)
                            onListClick(HomeListType.Bestseller.toString())
                        }

                        HomeListType.BlogBest -> {
                            viewModel.getSingleCategoryBookList("BlogBest", 100)
                            onListClick(HomeListType.BlogBest.toString())
                        }
                    }
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = mirroringIcon(
                        ltrIcon = Icons.Outlined.ArrowForward,
                        rtlIcon = Icons.Outlined.ArrowBack
                    ),
                    tint = BookDiaryTheme.colors.brand,
                    contentDescription = null
                )
            }
        }
        BookItemsRow(
            index = index,
            books = books,
            onBookClick = onBookClick,
        )
    }
}

@Composable
fun BookItemsRow(
    index: Int,
    books: LazyPagingItems<BookModel>,
    onBookClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val scroll = rememberScrollState(0)
    val gradientWidth = with(LocalDensity.current) {
        (6 * (cardWidth + cardPadding).toPx())
    }
    val gradient = when ((index / 2) % 2) { // 나누는 숫자로 gradient 종류 설정 가능 -> 추후 추가 고려
        0 -> BookDiaryTheme.colors.gradient6_1
        else -> BookDiaryTheme.colors.gradient2_2
    }
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp),
        userScrollEnabled = true
    ) {
        items(books.itemCount) {
            BookItemRow(
                books.itemSnapshotList[it],
                onBookClick,
                it,
                BookDiaryTheme.colors.gradient6_1,
                gradientWidth = gradientWidth,
                scroll.value
            )
        }
        books.apply {
            when {
                loadState.refresh is LoadState.Loading -> {}
                loadState.refresh is LoadState.Error -> {}
                loadState.append is LoadState.Loading -> {}
                loadState.append is LoadState.Error -> {}
            }
        }
    }
}

@Composable
fun BookItemRow(
    book: BookModel?,
    onBookClick: (Long) -> Unit,
    index: Int,
    gradient: List<Color>,
    gradientWidth: Float,
    scroll: Int,
    modifier: Modifier = Modifier
) {
    val left = index * with(LocalDensity.current) {
        (cardWidth + cardPadding).toPx()
    }

    BookCard(
        modifier = modifier
            .size(
                width = cardWidth,
                height = 265.dp
            )
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onBookClick(book?.itemId?.toLong() ?: 0) }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                val gradientOffSet = left - (scroll / 3f)
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .offsetGradientBackground(gradient, gradientWidth, gradientOffSet)
                )
                BookCoverImage(
                    imageUrl = book?.cover ?: "",
                    contentDescription = null,
                    modifier = Modifier
                        .width(140.dp)
                        .height(160.dp)
                        .align(Alignment.BottomCenter)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = book?.title ?: "",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                color = BookDiaryTheme.colors.textPrimary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = book?.author ?: "",
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium,
                color = BookDiaryTheme.colors.textHelp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}

@Composable
fun BookCoverImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp
) {
    BookImageCard(
        imageUrl = imageUrl,
        contentDescription = contentDescription ?: "",
        contentScale = ContentScale.FillHeight,
        elevation = elevation,
        border = BorderStroke(
            width = 1.dp,
            color = Color.White
        ),
        modifier = modifier,
        imageModifier = Modifier.fillMaxSize()
    )
}