package lee.project.presentation.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import lee.project.domain.book.model.BookModel
import lee.project.presentation.R
import lee.project.presentation.component.BasicButton
import lee.project.presentation.component.BasicDatePickerButton
import lee.project.presentation.component.BasicEditText
import lee.project.presentation.component.BookDiaryScaffold
import lee.project.presentation.theme.BookDiaryTheme
import lee.project.presentation.util.millToDate

/**
 * 내 책 기록 작성 dialog
 * 독서 기간 선택 버튼, 독후감 작성 editText
 * 독서 기간 미 선택시 오늘 날짜로 기록
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertMyBookDialog(
    bookInfo: BookModel,
    onDismiss: () -> Unit,
    onComplete: (String, String) -> Unit
) {
    var reviewText by remember { mutableStateOf("") }
    var periodText by remember { mutableStateOf("") }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        BookDiaryScaffold(
            modifier = Modifier
                .fillMaxWidth()
                .height(800.dp)
                .padding(horizontal = 15.dp),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.str_btn_record_title),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            color = BookDiaryTheme.colors.brand,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    actions = {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "close"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = BookDiaryTheme.colors.uiBackground
                    )
                )
            }
        ) {
            Box(
                modifier = Modifier
            ) {
                BasicDatePickerButton(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                        .background(
                            color = BookDiaryTheme.colors.uiBackground
                        ),
                    hint = periodText.ifEmpty { stringResource(id = R.string.str_record_date_hint) },
                    updateDate = {
                        periodText = it
                    }
                )
                BasicEditText(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize()
                        .padding(horizontal = 10.dp, vertical = 60.dp),
                    hint = stringResource(id = R.string.str_btn_record_detail),
                    preText = "",
                    errorModifier = Modifier
                        .height(30.dp),
                    updateText = {
                        reviewText = it
                    }
                )
                BasicButton(
                    onClick = {
                        if (periodText.isEmpty()) {
                            val today = millToDate(System.currentTimeMillis())
                            periodText = "$today ~ $today"
                        }
                        onComplete.invoke(reviewText, periodText)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(start = 30.dp, end = 30.dp, bottom = 10.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.str_btn_record),
                        color = BookDiaryTheme.colors.textSecondary,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }
        }
    }
}