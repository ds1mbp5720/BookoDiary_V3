package lee.project.presentation.record

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import lee.project.presentation.R
import lee.project.presentation.component.BasicButton
import lee.project.presentation.component.BookDiaryDivider
import lee.project.presentation.component.BookDiaryScaffold
import lee.project.presentation.component.BookDiarySurface
import lee.project.presentation.component.MyRecordDivider
import lee.project.presentation.component.SearchBar
import lee.project.presentation.component.SwipeToDismissVertical
import lee.project.presentation.navigation.BookDiaryBottomBar
import lee.project.presentation.navigation.MainSections
import lee.project.presentation.theme.BookDiaryTheme
import lee.project.presentation.util.textChangeVertical

@Composable
fun Record(
    onMyBookClick: (Long) -> Unit,
    onWishBookClick: (Long) -> Unit,
    onNavigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is RecordUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                is RecordUiEffect.NavigateToMyBookDetail -> {
                    onMyBookClick(effect.bookId)
                }
                is RecordUiEffect.NavigateToWishBookDetail -> {
                    onWishBookClick(effect.bookId)
                }
            }
        }
    }

    BookDiaryScaffold(
        bottomBar = {
            BookDiaryBottomBar(
                tabs = MainSections.entries.toTypedArray(),
                currentRoute = MainSections.RECORD.route,
                navigateToRoute = onNavigateToRoute
            )
        },
        modifier = modifier
    ) { paddingValues ->
        BookDiarySurface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BookDiaryTheme.colors.brandSecondary)
            ) {
                // 내 책 목록 섹션
                BookRecordContent(
                    animationVisible = uiState.recordVisibleType == RecordType.MYBOOK,
                    contentTitle = stringResource(id = R.string.str_record_info_title, uiState.myBookList.size),
                    books = uiState.myBookList.mapperMyBookToBasicBookRecordList().filter {
                        it.title.contains(uiState.query.text)
                    },
                    onBookClick = { id ->
                        viewModel.onEvent(RecordUiEvent.FindMyBook(id))
                    },
                    onBookDeleteSwipe = { id ->
                        viewModel.onEvent(RecordUiEvent.DeleteMyBook(id))
                    },
                    modifier = Modifier.fillMaxSize().zIndex(if (uiState.recordVisibleType == RecordType.MYBOOK) 1f else 0f)
                )

                // 찜 목록 섹션
                BookRecordContent(
                    animationVisible = uiState.recordVisibleType == RecordType.WISH,
                    contentTitle = stringResource(id = R.string.str_wish_title, uiState.wishBookList.size),
                    books = uiState.wishBookList.mapperWishBookToBasicBookRecordList().filter {
                        it.title.contains(uiState.query.text)
                    },
                    onBookClick = { id ->
                        viewModel.onEvent(RecordUiEvent.ClickWishBook(id))
                    },
                    onBookDeleteSwipe = { id ->
                        viewModel.onEvent(RecordUiEvent.DeleteWishBook(id))
                    },
                    modifier = Modifier.fillMaxSize().zIndex(if (uiState.recordVisibleType == RecordType.WISH) 1f else 0f)
                )

                SearchBar(
                    query = uiState.query,
                    onQueryChange = { viewModel.onEvent(RecordUiEvent.QueryChanged(it)) },
                    onSearch = { },
                    searchFocused = uiState.focused || uiState.query.text.isNotEmpty(),
                    onSearchFocusChange = { viewModel.onEvent(RecordUiEvent.FocusChanged(it)) },
                    onClearQuery = { viewModel.onEvent(RecordUiEvent.ClearQuery) },
                    searching = uiState.searching,
                    modifier = Modifier.align(Alignment.TopCenter).zIndex(2f)
                )

                BookDiaryDivider(modifier = Modifier.padding(top = 56.dp).zIndex(2f))

                SwipeContentButton(
                    contentType = uiState.recordVisibleType,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp, vertical = 70.dp)
                        .zIndex(2f),
                    onClick = {
                        val nextType = if (uiState.recordVisibleType == RecordType.MYBOOK) RecordType.WISH else RecordType.MYBOOK
                        viewModel.onEvent(RecordUiEvent.ChangeRecordType(nextType))
                    }
                )
            }
        }
    }
}

@Composable
fun SwipeContentButton(
    contentType: RecordType,
    modifier: Modifier,
    onClick: () -> Unit
) {
    BasicButton(
        onClick = onClick,
        modifier = modifier,
        border = BorderStroke(width = 1.dp, color = Color.Black)
    ) {
        Icon(
            modifier = Modifier.weight(1f),
            imageVector = if (contentType == RecordType.MYBOOK) Icons.Filled.Edit else Icons.Filled.ShoppingCart,
            contentDescription = "swipe_content"
        )
        Text(
            modifier = Modifier.weight(1f),
            text = if (contentType == RecordType.MYBOOK) stringResource(id = R.string.str_change_wish) else stringResource(
                id = R.string.str_change_record
            ),
            color = BookDiaryTheme.colors.textLink,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun BookRecordContent(
    animationVisible: Boolean,
    contentTitle: String,
    books: List<BasicBookRecord>?,
    onBookClick: (Long) -> Unit,
    onBookDeleteSwipe: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val offsetY by animateDpAsState(
        targetValue = if (animationVisible) 0.dp else 540.dp,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "offsetY"
    )
    val alpha by animateFloatAsState(
        targetValue = if (animationVisible) 1f else 0.9f,
        label = "alpha"
    )

    Box(
        modifier = modifier
            .padding(top = 60.dp)
            .offset(y = offsetY)
            .graphicsLayer(alpha = alpha)
    ) {
        MyRecordDivider(modifier = Modifier.fillMaxWidth())
        if (!books.isNullOrEmpty()) {
            BookRecordRow(
                books = books,
                onBookClick = onBookClick,
                onBookDeleteSwipe = onBookDeleteSwipe
            )
        } else {
            Spacer(modifier = Modifier.height(540.dp))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 24.dp)
                .background(color = Color.White)
                .border(width = 1.dp, color = BookDiaryTheme.colors.brand)
        ) {
            Text(
                text = contentTitle,
                style = MaterialTheme.typography.titleLarge,
                color = BookDiaryTheme.colors.brand,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.width(160.dp).padding(start = 16.dp)
            )
        }
        MyRecordDivider(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth())
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun BookRecordRow(
    books: List<BasicBookRecord>,
    onBookClick: (Long) -> Unit,
    onBookDeleteSwipe: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.height(540.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        items(books, key = { it.itemId }) { book ->
            val dismissState = rememberDismissState(
                confirmStateChange = { dismissValue ->
                    if (dismissValue == DismissValue.DismissedToStart) {
                        onBookDeleteSwipe(book.itemId)
                        true
                    } else false
                }
            )
            SwipeToDismissVertical(
                modifier = Modifier.animateItem(),
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                dismissThresholds = { FractionalThreshold(0.4f) },
                background = {
                    val color by animateColorAsState(
                        if (dismissState.targetValue == DismissValue.DismissedToStart) Color.Red else Color.Transparent,
                        label = ""
                    )
                    Box(
                        Modifier.fillMaxSize().background(color).padding(horizontal = 20.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", modifier = Modifier.scale(if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f))
                    }
                }
            ) {
                BookDraw(bookId = book.itemId, bookTitle = book.title, onBookClick = onBookClick)
            }
        }
    }
}

@Composable
fun BookDraw(
    bookId: Long,
    bookTitle: String,
    onBookClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val bookColor = when ((bookId % 4).toInt()) {
        0 -> Color(0xFFA73933)
        1 -> Color(0xFFDDAB88)
        2 -> Color(0xFF50A154)
        else -> Color(0xFF735549)
    }
    val labelColor = when ((bookId % 4).toInt()) {
        0 -> Color(0xFFFECC00)
        1 -> Color(0xFFD5CEC9)
        2 -> Color(0xFF353333)
        else -> Color(0xFF41393C)
    }
    val bookHeightRatio = when ((bookId % 4).toInt()) {
        0 -> 10f
        1 -> 20f
        2 -> 30f
        else -> 40f
    }
    Text(
        text = bookTitle.textChangeVertical(),
        style = MaterialTheme.typography.titleLarge,
        maxLines = 20,
        color = BookDiaryTheme.colors.textPrimary,
        textAlign = TextAlign.Center,
        modifier = modifier
            .border(1.dp, Color.Black, RoundedCornerShape(10))
            .width(75.dp)
            .height(540.dp - bookHeightRatio.dp)
            .background(color = bookColor, shape = RoundedCornerShape(10))
            .drawBehind {
                drawRect(
                    color = labelColor,
                    size = Size(width = 75.dp.toPx(), height = 20.dp.toPx()),
                    topLeft = Offset(x = 0f, y = bookHeightRatio)
                )
            }
            .clickable { onBookClick(bookId) }
    )
}
