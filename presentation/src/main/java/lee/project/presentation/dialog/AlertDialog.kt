package lee.project.presentation.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import lee.project.presentation.R
import lee.project.presentation.component.BasicButton
import lee.project.presentation.theme.BookDiaryTheme

@Composable
fun BookDiaryBasicDialog(
    title: String = "",
    body: String = "",
    dismissAction: () -> Unit,
    confirmAction: () -> Unit
) {
    AlertDialog(
        containerColor = BookDiaryTheme.colors.uiBackground,
        onDismissRequest = dismissAction,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = BookDiaryTheme.colors.textLink,
            )
        },
        text = {
            Text(
                text = body,
                style = MaterialTheme.typography.bodyLarge,
                color = BookDiaryTheme.colors.textLink,
            )
        },
        dismissButton = {
            BasicButton(
                onClick = dismissAction,
                modifier = Modifier,
                // colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            ) {
                Text(
                    text = stringResource(id = R.string.str_cancel),
                    modifier = Modifier.fillMaxWidth(),
                    color = BookDiaryTheme.colors.textSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            BasicButton(
                onClick = confirmAction,
                modifier = Modifier,
                //colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            ) {
                Text(
                    text = stringResource(id = R.string.str_confirm),
                    modifier = Modifier.fillMaxWidth(),
                    color = BookDiaryTheme.colors.textSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}