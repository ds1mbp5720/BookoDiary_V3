package lee.project.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import lee.project.presentation.theme.BookDiaryTheme
import lee.project.presentation.theme.Typography

@Composable
fun BasicEditText(
    modifier: Modifier = Modifier,
    hint: String,
    preText: String = "",
    isError: Boolean = false,
    errorText: String = "",
    errorModifier: Modifier = Modifier,
    updateText: (String) -> Unit
) {
    // 1. 상태 동기화: preText가 외부(부모)에서 변경되면 내부 상태도 함께 업데이트되도록 설정
    var inputText by remember(preText) { mutableStateOf(preText) }

    Column(modifier = modifier) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    // 2. 에러 상태일 때 테두리 색상 변경
                    color = if (isError) Color.Red else BookDiaryTheme.colors.brand,
                    shape = RoundedCornerShape(30.dp)
                ),
            // 3. 최신 Material3 TextFieldDefaults.colors() 적용
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                errorContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(30.dp),
            value = inputText,
            onValueChange = {
                inputText = it
                updateText.invoke(it)
            },
            isError = isError,
            singleLine = true,
            textStyle = Typography.labelLarge,
            placeholder = {
                Text(
                    text = hint,
                    style = Typography.labelLarge,
                    color = Color.Gray
                )
            }
        )

        // 4. 에러 메시지 레이아웃 개선
        if (isError && errorText.isNotEmpty()) {
            Text(
                modifier = errorModifier.padding(start = 16.dp, top = 4.dp),
                text = errorText,
                color = Color.Red,
                style = Typography.labelSmall
            )
        } else {
            Spacer(modifier = errorModifier.height(4.dp))
        }
    }
}