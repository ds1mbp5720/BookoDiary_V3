package lee.project.presentation.record

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import lee.project.presentation.R
import lee.project.presentation.component.BasicUpButton
import lee.project.presentation.component.BookDiarySurface
import lee.project.presentation.component.DetailHeader
import lee.project.presentation.detail.Image
import lee.project.presentation.detail.Title
import lee.project.presentation.detail.minTitleOffset
import lee.project.presentation.detail.titleHeight
import lee.project.presentation.theme.BookDiaryTheme
import lee.project.presentation.theme.ReviewPaperColor

@Composable
fun RecordDetailScreen(
    bookId: Long,
    upPress: () -> Unit,
    modifier: Modifier = Modifier,
    recordViewModel: RecordViewModel = hiltViewModel()
) {
    val uiState by recordViewModel.uiState.collectAsStateWithLifecycle()

    // 화면 진입 시 해당 ID의 기록 찾기
    LaunchedEffect(bookId) {
        recordViewModel.onEvent(RecordUiEvent.FindMyBook(bookId))
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        val scroll = rememberScrollState(0)
        DetailHeader()
        
        uiState.myBookDetail?.let { bookDetailInfo ->
            Body(
                myReview = bookDetailInfo.myReview ?: "기록한 리뷰가 없습니다.",
                scroll = scroll,
                modifier = Modifier.fillMaxWidth()
            )
            Title(
                title = bookDetailInfo.title,
                author = bookDetailInfo.author ?: "저자 미확인",
                period = bookDetailInfo.period ?: ""
            ) {
                scroll.value
            }
            Image(imageUrl = bookDetailInfo.imageUrl) {
                scroll.value
            }
        } ?: run {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "책 정보가 없습니다.",
                    color = BookDiaryTheme.colors.textHelp
                )
            }
        }
        BasicUpButton(upPress)
    }
}

@Composable
private fun Body(
    myReview: String,
    scroll: ScrollState,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(minTitleOffset)
        )
        Column(
            modifier = Modifier.verticalScroll(scroll),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BookDiarySurface(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(7.dp)
                        )
                        .background(
                            color = ReviewPaperColor,
                            shape = RoundedCornerShape(7.dp)
                        )
                ) {
                    Spacer(modifier = Modifier.height(titleHeight + 40.dp))
                    Text(
                        text = stringResource(id = R.string.str_record_title),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        color = BookDiaryTheme.colors.textPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                    )
                    Text(
                        text = myReview,
                        style = MaterialTheme.typography.bodyLarge,
                        color = BookDiaryTheme.colors.textPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp, vertical = 20.dp)
                            .padding(
                                bottom = if (myReview.length < 500) 400.dp else 40.dp
                            ),
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreViewBody() {
    BookDiaryTheme {
        Body(
            myReview = "이 책은 정말 감명 깊었습니다. 여러 번 다시 읽고 싶은 책이네요.",
            scroll = rememberScrollState(0)
        )
    }
}
