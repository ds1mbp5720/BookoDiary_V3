package lee.project.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import lee.project.presentation.theme.BookDiaryTheme

@Composable
fun BasicSwitch(
    text: String,
    checked: Boolean,
    onChecked: (Boolean) -> Unit
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            modifier = Modifier,
            text = text,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Start,
            color = BookDiaryTheme.colors.textPrimary
        )
        Switch(
            modifier = Modifier,
            checked = checked,
            onCheckedChange = onChecked,
            colors = SwitchDefaults.colors(
                checkedThumbColor = BookDiaryTheme.colors.brand,
                checkedTrackColor = BookDiaryTheme.colors.brandSecondary,
                uncheckedThumbColor = BookDiaryTheme.colors.textInteractive,
                uncheckedTrackColor = BookDiaryTheme.colors.uiBorder,
            )
        )
    }

}