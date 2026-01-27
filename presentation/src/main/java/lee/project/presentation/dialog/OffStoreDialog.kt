package lee.project.presentation.dialog

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import lee.project.domain.offstore.model.OffStoreListModel
import lee.project.presentation.R
import lee.project.presentation.component.BookDiaryScaffold
import lee.project.presentation.theme.BookDiaryTheme

@Composable
fun OffStoreDialog(
    offStoreInfo: OffStoreListModel?,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismiss) {
        BookDiaryScaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.str_dialog_offstore_title),
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
                    backgroundColor = BookDiaryTheme.colors.uiBackground
                )
            }
        ) {
            if (offStoreInfo?.itemOffStoreList?.isEmpty() == true) {
                Text(
                    modifier = Modifier.fillMaxSize(),
                    text = stringResource(id = R.string.str_no_off_store_result),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
                userScrollEnabled = true
            ) {
                items(offStoreInfo?.itemOffStoreList?.size ?: 0) {
                    val offStore = offStoreInfo?.itemOffStoreList!![it]
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 6.dp, vertical = 6.dp)
                            .fillMaxWidth()
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(offStore.link))
                                context.startActivity(intent)
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        colors = CardDefaults.cardColors(containerColor = BookDiaryTheme.colors.uiBackground),
                        shape = RoundedCornerShape(corner = CornerSize(16.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(start = 4.dp, end = 4.dp, top = 6.dp, bottom = 6.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${offStore.offName}",
                                style = MaterialTheme.typography.labelLarge,
                                color = BookDiaryTheme.colors.textLink
                            )
                        }
                    }
                }
            }
        }
    }
}