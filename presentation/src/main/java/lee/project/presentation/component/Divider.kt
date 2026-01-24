package lee.project.presentation.component

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import lee.project.presentation.theme.BookDiaryTheme

@Composable
fun BookDiaryDivider(
    modifier: Modifier = Modifier,
    color: Color = BookDiaryTheme.colors.uiBorder.copy(alpha = 0.12f),
    thickness: Dp = 1.dp
) {
    HorizontalDivider(
        modifier = modifier,
        color = color,
        thickness = thickness,
    )
}

@Composable
fun MyRecordDivider(
    modifier: Modifier = Modifier,
    color: Color = BookDiaryTheme.colors.brand,
    thickness: Dp = 10.dp
) {
    HorizontalDivider(
        modifier = modifier,
        color = color,
        thickness = thickness
    )
}
