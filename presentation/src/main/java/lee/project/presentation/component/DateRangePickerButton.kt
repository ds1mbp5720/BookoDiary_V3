package lee.project.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import lee.project.presentation.R
import lee.project.presentation.theme.BookDiaryTheme
import lee.project.presentation.util.millToDate

/**
 * 기간 선택 Dialog 노출 compose 버튼
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicDatePickerButton(
    modifier: Modifier,
    hint: String,
    updateDate: (String) -> Unit
) {
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    val dateRangeState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = System.currentTimeMillis(),
        initialSelectedEndDateMillis = System.currentTimeMillis(),
        initialDisplayMode = DisplayMode.Picker
    )
    val openDateDialog = remember { mutableStateOf(false) }
    BasicButton(
        onClick = {
            openDateDialog.value = true
        },
        modifier = modifier,
        border = BorderStroke(
            width = 1.dp,
            color = BookDiaryTheme.colors.brand
        ),
        backgroundGradient = listOf(
            BookDiaryTheme.colors.uiBackground,
            BookDiaryTheme.colors.uiBackground
        )
    ) {
        Text(
            text = hint,
            color = BookDiaryTheme.colors.textPrimary,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
    if (openDateDialog.value) {
        Dialog(
            onDismissRequest = {
                openDateDialog.value = false
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            BookDiaryScaffold(
                modifier = Modifier
                    .height(500.dp)
                    .padding(horizontal = 15.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    DateRangePicker(
                        state = dateRangeState,
                        modifier = Modifier,
                        title = {},
                        headline = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                (if (dateRangeState.selectedStartDateMillis != null) {
                                    dateRangeState.selectedStartDateMillis?.let { millToDate(it) }
                                } else {
                                    stringResource(id = R.string.str_start_date)
                                })?.let {
                                    startDate = it
                                    Text(text = it)
                                }
                                Text(text = "~")
                                (if (dateRangeState.selectedEndDateMillis != null) {
                                    dateRangeState.selectedEndDateMillis?.let { millToDate(it) }
                                } else {
                                    stringResource(id = R.string.str_end_date)
                                })?.let {
                                    endDate = it
                                    Text(text = it)
                                }
                            }
                        },
                        showModeToggle = true,
                        colors = DatePickerDefaults.colors(
                            containerColor = Color.Blue,
                            titleContentColor = Color.Black,
                            headlineContentColor = Color.Black,
                            weekdayContentColor = Color.Black,
                            subheadContentColor = Color.Black,
                            yearContentColor = Color.Green,
                            currentYearContentColor = Color.Red,
                            selectedYearContainerColor = Color.Red,
                            disabledDayContentColor = Color.Gray,
                            todayDateBorderColor = Color.Blue,
                            dayInSelectionRangeContainerColor = Color.LightGray,
                            dayInSelectionRangeContentColor = Color.White,
                            selectedDayContainerColor = Color.Black
                        )
                    )
                    Button(
                        onClick = {
                            updateDate.invoke("$startDate ~ $endDate")
                            openDateDialog.value = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 16.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.str_complete),
                            color = Color.White
                        )
                    }
                }

            }
        }
    }
}