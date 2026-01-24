package lee.project.presentation.book

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import lee.project.domain.book.model.MyBookModel
import lee.project.presentation.component.BookDiarySurface
import lee.project.presentation.theme.BookDiaryTheme

private val componentHeight = 120.dp
private val textWidth = 180.dp

/**
 * 메인화면 상단쪽 MyBook Row 형 간단 정보 표시 item
 * 표지, 제목, 지은이, 독서기간 정보 제공
 * 클릭시 상세보기 이동
 */
@Composable
fun MyBookRowItem(
    modifier: Modifier = Modifier,
    myBook: MyBookModel,
    onMyBookClick: (String) -> Unit
) {
    BookDiarySurface(
        color = Color.White,
        elevation = 3.dp,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .clickable { onMyBookClick(myBook.itemId) }
            .padding(10.dp),
    ) {
        Row {
            BookCoverImage(
                imageUrl = myBook.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .height(componentHeight)
                    .padding(horizontal = 7.dp, vertical = 7.dp)
            )
            Box(
                modifier = Modifier
                    .size(textWidth, componentHeight)
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = myBook.title,
                        style = MaterialTheme.typography.titleLarge,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        color = BookDiaryTheme.colors.textPrimary,
                        modifier = Modifier
                    )
                    Text(
                        text = myBook.author,
                        style = MaterialTheme.typography.titleMedium,
                        color = BookDiaryTheme.colors.textPrimary,
                        maxLines = 2,
                        modifier = Modifier
                    )
                }
                Text(
                    text = myBook.period,
                    style = MaterialTheme.typography.bodyLarge,
                    color = BookDiaryTheme.colors.textLink,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                )
            }
        }
    }
}