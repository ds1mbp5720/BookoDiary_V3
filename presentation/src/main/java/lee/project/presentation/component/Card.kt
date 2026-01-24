package lee.project.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import lee.project.presentation.R
import lee.project.presentation.theme.BookDiaryTheme

/**
 * 리스트 내부 책 정보 UI 포장 목적 View
 */
@Composable
fun BookCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    color: Color = BookDiaryTheme.colors.uiBackground,
    contentColor: Color = BookDiaryTheme.colors.textPrimary,
    border: BorderStroke? = null,
    elevation: Dp = 4.dp,
    content: @Composable () -> Unit
) {
    BookDiarySurface(
        modifier = modifier,
        shape = shape,
        color = color,
        contentColor = contentColor,
        border = border,
        elevation = elevation,
        content = content
    )
}

@Composable
fun BookImageCard(
    imageUrl: String,
    contentDescription: String = "",
    contentScale: ContentScale = ContentScale.FillHeight,
    elevation: Dp = 0.dp,
    border: BorderStroke? = null,
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
) {
    BookDiarySurface(
        color = Color.White,
        elevation = elevation,
        shape = RectangleShape,
        modifier = modifier,
        border = border
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = imageModifier,
            placeholder = painterResource(id = R.drawable.book_24),
            error = painterResource(id = R.drawable.book_24)
        )
    }
}
