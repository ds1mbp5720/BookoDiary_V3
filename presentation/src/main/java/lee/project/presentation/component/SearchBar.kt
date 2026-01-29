package lee.project.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lee.project.presentation.R
import lee.project.presentation.theme.BookDiaryTheme
import lee.project.presentation.util.mirroringBackIcon

/**
 * 검색 Bar
 * 검색어 입력 및 CircularProgressIndicator 를 통한 통신 진행 표시
 * 검색 버튼, 키보드 검색 버튼 동일
 * needs: viewModel 의 searchState class 필요
 */
@Composable
fun SearchBar(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onSearch: () -> Unit,
    searchFocused: Boolean,
    onSearchFocusChange: (Boolean) -> Unit,
    onClearQuery: () -> Unit,
    searching: Boolean,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    BookDiarySurface(
        color = BookDiaryTheme.colors.uiFloated,
        contentColor = BookDiaryTheme.colors.textSecondary,
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (query.text.isEmpty()) SearchHint()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight()
            ) {
                if (searchFocused) {
                    IconButton(onClick = onClearQuery) {
                        Icon(
                            imageVector = mirroringBackIcon(),
                            tint = BookDiaryTheme.colors.iconPrimary,
                            contentDescription = "ic_back"
                        )
                    }
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onSearch.invoke()
                            keyboardController?.hide()
                        }
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged {
                            onSearchFocusChange(it.isFocused)
                        }
                )
                if (searchFocused) {
                    IconButton(
                        onClick = {
                            onSearch.invoke()
                            keyboardController?.hide()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            tint = BookDiaryTheme.colors.iconPrimary,
                            contentDescription = ""
                        )
                    }
                }
                if (searching) {
                    CircularProgressIndicator(
                        color = BookDiaryTheme.colors.iconPrimary,
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                            .size(36.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SearchHint() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
    ) {
        Icon(
            imageVector = Icons.Outlined.Search,
            tint = BookDiaryTheme.colors.textHelp,
            contentDescription = "ic_search",
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(id = R.string.str_search_hint),
            color = BookDiaryTheme.colors.textHelp
        )
    }
}

@Preview(showBackground = true, name = "기본 상태")
@Composable
fun PreSearchBar() {
    BookDiaryTheme {
        SearchBar(
            query = TextFieldValue(""),
            onQueryChange = {},
            onSearch = {},
            searchFocused = false,
            onSearchFocusChange = {},
            onClearQuery = {},
            searching = false
        )
    }
}

@Preview(showBackground = true, name = "입력 상태")
@Composable
fun PreSearchBarFocused() {
    BookDiaryTheme {
        SearchBar(
            query = TextFieldValue("안드로이드"),
            onQueryChange = {},
            onSearch = {},
            searchFocused = true,
            onSearchFocusChange = {},
            onClearQuery = {},
            searching = false
        )
    }
}
