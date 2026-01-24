package lee.project.presentation.book

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import lee.project.domain.book.model.BookModel
import lee.project.presentation.R
import lee.project.presentation.component.BookDiaryDivider
import lee.project.presentation.theme.BookDiaryTheme
import lee.project.presentation.util.addCommaWon

/**
 * 세로형 책 List
 */
@Composable
fun BookItemList(
    book: BookModel?,
    onBookClick: (Long) -> Unit,
    showDivider: Boolean,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onBookClick(book?.itemId?.toLong() ?: 0) }
            .padding(horizontal = 24.dp)
    ) {
        val (divider, image, title, price, author) = createRefs()
        createVerticalChain(title, price, author, chainStyle = ChainStyle.Packed)
        if (showDivider) {
            BookDiaryDivider(
                modifier = Modifier.constrainAs(divider) {
                    linkTo(start = parent.start, end = parent.end)
                    top.linkTo(parent.top)
                }
            )
        }
        BookCoverImage(
            imageUrl = book?.cover ?: "",
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .constrainAs(image) {
                    linkTo(
                        top = parent.top,
                        topMargin = 16.dp,
                        bottom = parent.bottom,
                        bottomMargin = 16.dp
                    )
                    start.linkTo(parent.start)
                }
        )
        Text(
            text = book?.title ?: stringResource(id = R.string.str_no_title),
            style = MaterialTheme.typography.titleLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            color = BookDiaryTheme.colors.textPrimary,
            modifier = Modifier
                .width(250.dp)
                .constrainAs(title) {
                    linkTo(
                        start = image.end,
                        startMargin = 16.dp,
                        end = parent.end,
                        endMargin = 16.dp,
                        bias = 0f
                    )
                }
        )
        Text(
            text = book?.author ?: stringResource(id = R.string.str_no_author),
            style = MaterialTheme.typography.titleMedium,
            color = BookDiaryTheme.colors.textPrimary,
            maxLines = 2,
            modifier = Modifier
                .width(250.dp)
                .constrainAs(author) {
                    linkTo(
                        start = image.end,
                        startMargin = 16.dp,
                        end = parent.end,
                        endMargin = 16.dp,
                        bias = 0f
                    )
                }
        )
        Text(
            text = book?.priceSales?.addCommaWon() ?: stringResource(id = R.string.str_no_price),
            style = MaterialTheme.typography.bodyLarge,
            color = BookDiaryTheme.colors.textLink,
            modifier = Modifier.constrainAs(price) {
                linkTo(
                    start = image.end,
                    startMargin = 16.dp,
                    end = parent.end,
                    endMargin = 16.dp,
                    bias = 0f
                )
                top.linkTo(author.bottom, margin = 8.dp)
            }
        )
    }
}