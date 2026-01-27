package lee.project.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp
import lee.project.presentation.book.BookCoverImage
import lee.project.presentation.component.BookDiaryDivider
import lee.project.presentation.theme.BookDiaryTheme
import lee.project.presentation.util.addCommaWon
import java.lang.Integer.max
import java.lang.Integer.min

val titleHeight = 120.dp
val gradientScroll = 230.dp // 스크롤을 위한 간격
val imageOverlap = 115.dp // 상단 정보와 Top 간격 ( scroll 시 사라지는 부분)
val minTitleOffset = 85.dp
val minImageOffset = 42.dp // 이미지 최소 크기일때 padding Top
val maxTitleOffset = imageOverlap + minTitleOffset + gradientScroll
val expandedImageSize = 300.dp // 첫 이미지 사이즈(최대)
val collapsedImageSize = 100.dp // 이미지 축소 사이즈
val horizontalPadding = Modifier.padding(horizontal = 24.dp)

/**
 * 상세보기 (일반리스트, 내 기록) 공용 컴포넌트
 */

@Composable
fun Title(
    title: String?,
    author: String?,
    priceStandard: String? = null,
    period: String? = null,
    scrollProvider: () -> Int
) {
    val maxOffset = with(LocalDensity.current) { maxTitleOffset.toPx() }
    val minOffset = with(LocalDensity.current) { minTitleOffset.toPx() }
    val collapseRange = with(LocalDensity.current) { (maxTitleOffset - minTitleOffset).toPx() }
    val collapseFractionProvider = {
        (scrollProvider() / collapseRange).coerceIn(0f, 1f)
    }

    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .heightIn(min = titleHeight)
            .statusBarsPadding()
            .offset {
                val scroll = scrollProvider()
                val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
                IntOffset(x = 0, y = offset.toInt())
            }
            .background(color = BookDiaryTheme.colors.uiBackground)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Layout(
            content = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = title?.replace("알라딘 상품정보 - ", "") ?: "No Title",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        color = BookDiaryTheme.colors.textPrimary,
                        modifier = horizontalPadding
                            .fillMaxWidth()
                            .padding(bottom = 5.dp)
                    )
                    Text(
                        text = author ?: "지은이 미확인",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        color = BookDiaryTheme.colors.textPrimary,
                        modifier = horizontalPadding
                            .fillMaxWidth()
                            .padding(bottom = 5.dp)
                    )
                    if (priceStandard != null) {
                        Row(
                            modifier = horizontalPadding.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "정가:",
                                style = MaterialTheme.typography.labelSmall,
                                color = BookDiaryTheme.colors.textPrimary
                            )
                            Text(
                                text = (priceStandard ?: "0").addCommaWon(),
                                style = MaterialTheme.typography.displaySmall,
                                color = BookDiaryTheme.colors.textPrimary
                            )
                        }
                    }
                    if (period != null) {
                        Text(
                            text = period,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            modifier = horizontalPadding
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .background(
                                    color = BookDiaryTheme.colors.uiBackground,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(8.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    BookDiaryDivider()
                }
            }
        ) { measurableList, constraints ->
            val collapseFraction = collapseFractionProvider()
            val titleMaxSize = constraints.maxWidth
            val titleMinSize =
                constraints.maxWidth - max(collapsedImageSize.roundToPx(), constraints.minWidth)
            val titleWidth = lerp(titleMaxSize, titleMinSize, collapseFraction)
            val titlePlaceable =
                measurableList[0].measure(Constraints.fixed(titleWidth, titleHeight.toPx().toInt()))
            layout(
                width = constraints.maxWidth,
                height = minTitleOffset.toPx().toInt()
            ) {
                titlePlaceable.placeRelative(0, 0)
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun Image(
    imageUrl: String,
    scrollProvider: () -> Int
) {
    val collapseRange = with(LocalDensity.current) { (maxTitleOffset - minTitleOffset).toPx() }
    val collapseFractionProvider = {
        (scrollProvider() / collapseRange).coerceIn(0f, 1f)
    }
    CollapsingImageLayout(
        collapseFractionProvider = collapseFractionProvider,
        modifier = horizontalPadding.then(Modifier.statusBarsPadding())
    ) {
        BookCoverImage(
            imageUrl = imageUrl,
            contentDescription = "detail_cover",
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

// scroll 하단 이동시 이미지 size 변경
@Composable
fun CollapsingImageLayout(
    collapseFractionProvider: () -> Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        check(measurables.size == 1)

        val collapseFraction = collapseFractionProvider()

        val imageMaxSize = min(expandedImageSize.roundToPx(), constraints.maxWidth)
        val imageMinSize = max(collapsedImageSize.roundToPx(), constraints.minWidth)
        val imageWidth = lerp(imageMaxSize, imageMinSize, collapseFraction)// - 80
        val imagePlaceable = measurables[0].measure(Constraints.fixed(imageWidth, imageWidth + 140))

        val imageY = lerp(minTitleOffset, minImageOffset, collapseFraction).roundToPx()
        val imageX = lerp(
            (constraints.maxWidth - imageWidth) / 2,
            constraints.maxWidth - imageWidth,
            collapseFraction
        )
        layout(
            width = constraints.maxWidth,
            height = imageY + imageWidth
        ) {
            imagePlaceable.placeRelative(imageX, imageY)
        }
    }
}