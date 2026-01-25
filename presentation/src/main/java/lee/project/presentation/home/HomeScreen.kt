package lee.project.presentation.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import lee.project.domain.book.model.BookModel
import lee.project.domain.book.model.MyBookModel
import lee.project.presentation.R
import lee.project.presentation.book.BookItemList
import lee.project.presentation.book.BookRowContent
import lee.project.presentation.book.MyBookRowItem
import lee.project.presentation.component.BasicUpButton
import lee.project.presentation.component.BookDiaryDivider
import lee.project.presentation.component.BookDiaryScaffold
import lee.project.presentation.component.BookDiarySurface
import lee.project.presentation.dialog.BookDiaryBasicDialog
import lee.project.presentation.navigation.BookDiaryBottomBar
import lee.project.presentation.navigation.MainSections
import lee.project.presentation.theme.BookDiaryTheme
import lee.project.presentation.util.mirroringIcon
import androidx.core.net.toUri

@Composable
fun Home(
    onBookClick: (Long) -> Unit,
    onMyBookClick: (String) -> Unit,
    onListClick: (String) -> Unit,
    onNavigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as Activity
    val showFinishDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeScreenEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    BookDiaryScaffold(
        bottomBar = {
            BookDiaryBottomBar(
                tabs = MainSections.entries.toTypedArray(),
                currentRoute = MainSections.HOME.route,
                navigateToRoute = onNavigateToRoute
            )
        },
        modifier = modifier
    ) { paddingValues ->
        HomeScreenContent(
            state = state,
            onEvent = viewModel::onEvent,
            onBookClick = onBookClick,
            onMyBookClick = onMyBookClick,
            onListClick = onListClick,
            modifier = Modifier.padding(paddingValues)
        )

        BackHandler(enabled = true) {
            showFinishDialog.value = true
        }

        if (showFinishDialog.value) {
            BookDiaryBasicDialog(
                title = stringResource(id = R.string.str_dialog_exit_app),
                dismissAction = { showFinishDialog.value = false },
                confirmAction = {
                    showFinishDialog.value = false
                    activity.finish()
                }
            )
        }
    }
}

@Composable
private fun HomeScreenContent(
    state: HomeScreenState,
    onEvent: (HomeScreenEvent) -> Unit,
    onBookClick: (Long) -> Unit,
    onMyBookClick: (String) -> Unit,
    onListClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // 최상단에서 페이징 아이템 수집
    val itemNewAll = state.bookListDataItemNewAll.collectAsLazyPagingItems()
    val itemNewSpecial = state.bookListDataItemNewSpecial.collectAsLazyPagingItems()
    val bestseller = state.bookListDataBestseller.collectAsLazyPagingItems()
    val blogBest = state.bookListDataBlogBest.collectAsLazyPagingItems()

    BookDiarySurface(modifier = modifier.fillMaxSize()) {
        LazyColumn {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                AladinLogo()
                BookDiaryDivider(thickness = 2.dp)
            }
            item {
                RecordInfoView(
                    myBookList = state.myBookList,
                    onMyBookClick = onMyBookClick
                )
            }
            // 각 섹션을 독립적인 item으로 분리하여 레이아웃 안정성 확보
            item {
                BookRowSection(HomeListType.ItemNewAll, itemNewAll, onBookClick, onListClick, onEvent)
                BookDiaryDivider(thickness = 2.dp)
            }
            item {
                BookRowSection(HomeListType.ItemNewSpecial, itemNewSpecial, onBookClick, onListClick, onEvent)
                BookDiaryDivider(thickness = 2.dp)
            }
            item {
                BookRowSection(HomeListType.Bestseller, bestseller, onBookClick, onListClick, onEvent)
                BookDiaryDivider(thickness = 2.dp)
            }
            item {
                BookRowSection(HomeListType.BlogBest, blogBest, onBookClick, onListClick, onEvent)
            }
        }
    }
}

@Composable
private fun RecordInfoView(
    myBookList: List<MyBookModel>,
    onMyBookClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.padding(start = 24.dp, top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.str_home_record_info_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = BookDiaryTheme.colors.textPrimary,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { /* TODO: Move to record screen */ }) {
                Icon(
                    imageVector = mirroringIcon(
                        ltrIcon = Icons.Outlined.ArrowForward,
                        rtlIcon = Icons.Outlined.ArrowBack
                    ),
                    tint = BookDiaryTheme.colors.brand,
                    contentDescription = null
                )
            }
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(myBookList.size) { index ->
                MyBookRowItem(
                    myBook = myBookList[index],
                    onMyBookClick = onMyBookClick
                )
            }
        }
    }
}

@Composable
private fun BookRowSection(
    type: HomeListType,
    books: LazyPagingItems<BookModel>,
    onBookClick: (Long) -> Unit,
    onListClick: (String) -> Unit,
    onEvent: (HomeScreenEvent) -> Unit
) {
    BookRowContent(
        contentTitle = type,
        books = books,
        onBookClick = onBookClick,
        onListClick = {
            onEvent(HomeScreenEvent.LoadCategoryPaging(type.name))
            onListClick(type.name)
        }
    )
}

@Composable
fun AladinLogo(url: String = "https://image.aladin.co.kr/img/header/2011/aladin_logo_new.gif") {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW,
                    "https://www.aladin.co.kr/home/welcome.aspx".toUri())
                context.startActivity(intent)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = url,
                contentDescription = "aladinLogo",
                contentScale = ContentScale.Inside,
                modifier = Modifier.wrapContentSize()
            )
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Text(
                text = "도서 DB 제공 : 알라딘 인터넷서점",
                style = MaterialTheme.typography.titleMedium,
                color = BookDiaryTheme.colors.textLink
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    BookDiaryTheme {
        HomeScreenContent(
            state = HomeScreenState(
                myBookList = listOf(
                    MyBookModel(
                        itemId = "1",
                        title = "Preview Book 1",
                        author = "Author 1",
                        imageUrl = "",
                        period = "2024.01.01 ~ 2024.01.02",
                        rating = 4.5f,
                        link = null,
                        myReview = null
                    )
                )
            ),
            onEvent = {},
            onBookClick = {},
            onMyBookClick = {},
            onListClick = {}
        )
    }
}
