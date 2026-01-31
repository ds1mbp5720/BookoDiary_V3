package lee.project.presentation.detail

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import lee.project.domain.book.model.BookListModel
import lee.project.domain.book.model.BookModel
import lee.project.domain.book.model.MyBookModel
import lee.project.domain.book.model.WishBookModel
import lee.project.presentation.R
import lee.project.presentation.component.BasicButton
import lee.project.presentation.component.BasicUpButton
import lee.project.presentation.component.BookDiaryDivider
import lee.project.presentation.component.BookDiarySurface
import lee.project.presentation.component.BookImageCard
import lee.project.presentation.component.DetailHeader
import lee.project.presentation.component.RatingBar
import lee.project.presentation.dialog.DialogVisibleAnimate
import lee.project.presentation.dialog.InsertMyBookDialog
import lee.project.presentation.dialog.OffStoreDialog
import lee.project.presentation.theme.BookDiaryTheme

val bottomBarHeight = 56.dp

@Composable
fun BookDetail(
    bookId: Long,
    upPress: () -> Unit,
    bookDetailViewModel: BookDetailViewModel = hiltViewModel()
) {
    val uiState by bookDetailViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(bookId) {
        bookDetailViewModel.onEvent(BookDetailUiEvent.LoadBookDetail(bookId))
    }

    LaunchedEffect(Unit) {
        bookDetailViewModel.effect.collect { effect ->
            when (effect) {
                is BookDetailUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    BookDetailContent(
        uiState = uiState,
        upPress = upPress,
        onOffStoreInfo = { bookDetailViewModel.onEvent(BookDetailUiEvent.LoadOffStoreInfo(bookId.toString())) },
        onWishBook = { book -> bookDetailViewModel.onEvent(BookDetailUiEvent.AddWishBook(book)) },
        onMyBook = { book -> bookDetailViewModel.onEvent(BookDetailUiEvent.AddMyBook(book)) }
    )
}

@Composable
private fun BookDetailContent(
    uiState: BookDetailUiState,
    upPress: () -> Unit,
    onOffStoreInfo: () -> Unit,
    onWishBook: (WishBookModel) -> Unit,
    onMyBook: (MyBookModel) -> Unit
) {
    var offStoreDialogVisible by rememberSaveable { mutableStateOf(false) }
    var insertMyBookDialogVisible by rememberSaveable { mutableStateOf(false) }
    val scroll = rememberScrollState(0)

    Box(modifier = Modifier.fillMaxSize()) {
        DetailHeader()

        uiState.bookDetail?.let { bookDetailInfo ->
            val bookDetail = bookDetailInfo.bookList[0]
            Body(
                book = bookDetail,
                url = bookDetail.link ?: "",
                scroll = scroll
            ) {
                onOffStoreInfo()
                offStoreDialogVisible = true
            }
            Title(
                title = bookDetail.title,
                author = bookDetail.author,
                priceStandard = bookDetail.priceStandard,
            ) {
                scroll.value
            }
            Image(imageUrl = bookDetail.cover ?: "") { scroll.value }
            DetailBottomBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                insertMyBook = { insertMyBookDialogVisible = true },
                insertWishBook = {
                    onWishBook(
                        WishBookModel(
                            itemId = bookDetail.itemId ?: 0,
                            imageUrl = bookDetail.cover ?: "",
                            title = bookDetail.title ?: "제목 없음",
                            addedAt = System.currentTimeMillis()

                        )
                    )
                }
            )
            DialogVisibleAnimate(visible = insertMyBookDialogVisible) {
                InsertMyBookDialog(
                    bookInfo = bookDetail,
                    onDismiss = { insertMyBookDialogVisible = false }
                ) { review, period ->
                    onMyBook(
                        MyBookModel(
                            itemId = bookDetail.itemId ?: 0,
                            imageUrl = bookDetail.cover ?: "",
                            title = bookDetail.title ?: "제목 없음",
                            author = bookDetail.author ?: "저자 미확인",
                            link = bookDetail.link,
                            myReview = review,
                            period = period,
                            rating = 0f
                        )
                    )
                    insertMyBookDialogVisible = false
                }
            }
        }

        BasicUpButton(upPress)

        DialogVisibleAnimate(visible = offStoreDialogVisible) {
            uiState.offStoreInfo?.let { offStoreInfo ->
                OffStoreDialog(offStoreInfo = offStoreInfo) {
                    offStoreDialogVisible = false
                }
            }
        }
    }
}

@Composable
private fun Body(
    book: BookModel,
    url: String,
    scroll: ScrollState,
    offStoreInfo: () -> Unit
) {
    val context = LocalContext.current
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(minTitleOffset)
        )
        Column(
            modifier = Modifier.verticalScroll(scroll)
        ) {
            Spacer(modifier = Modifier.height(gradientScroll))
            BookDiarySurface(Modifier.fillMaxWidth()) {
                Column {
                    Spacer(modifier = Modifier.height(imageOverlap + titleHeight + 20.dp))
                    DetailSubInfoRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        page = book.subInfo?.itemPage,
                        stockStatus = book.stockStatus,
                        ratingCnt = book.subInfo?.ratingInfo?.ratingCount
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = stringResource(id = R.string.str_detail_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = BookDiaryTheme.colors.textHelp,
                        modifier = horizontalPadding
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    var seeMore by remember { mutableStateOf(true) }
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        text = if (book.description != "") book.description ?: stringResource(id = R.string.str_no_detail)
                        else stringResource(id = R.string.str_no_detail),
                        color = BookDiaryTheme.colors.textPrimary,
                        maxLines = if (seeMore) 2 else Int.MAX_VALUE,
                        overflow = TextOverflow.Ellipsis,
                        modifier = horizontalPadding
                    )
                    val textButton =
                        if (seeMore) stringResource(id = R.string.str_see_more)
                        else stringResource(id = R.string.str_see_less)
                    Text(
                        text = textButton,
                        style = MaterialTheme.typography.bodyLarge,
                        color = BookDiaryTheme.colors.textLink,
                        modifier = Modifier
                            .heightIn(20.dp)
                            .fillMaxWidth()
                            .padding(top = 15.dp, start = 40.dp)
                            .clickable { seeMore = !seeMore }
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    DetailInfoColumn(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        title = stringResource(id = R.string.str_category),
                        detail = book.categoryName,
                        exceptionDetail = stringResource(id = R.string.str_no_category)
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    DetailInfoColumn(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        title = stringResource(id = R.string.str_publisher),
                        detail = book.publisher,
                        exceptionDetail = stringResource(id = R.string.str_no_publisher)
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    DetailInfoColumn(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        title = stringResource(id = R.string.str_pub_date),
                        detail = book.pubDate,
                        exceptionDetail = stringResource(id = R.string.str_no_pub_date)
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    DetailInfoColumn(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        title = stringResource(id = R.string.str_bestseller_rank),
                        detail = book.subInfo?.bestSellerRank,
                        exceptionDetail = stringResource(id = R.string.str_no_bestseller)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(id = R.string.str_rating_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = BookDiaryTheme.colors.textHelp,
                        modifier = horizontalPadding
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val ratingText =
                        "별 평점 : " + book.subInfo?.ratingInfo?.ratingScore + " / 리뷰 수 : " + book.subInfo?.ratingInfo?.ratingCount
                    Text(
                        text = ratingText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = BookDiaryTheme.colors.textHelp,
                        modifier = horizontalPadding
                    )
                    RatingBar(
                        modifier = horizontalPadding,
                        context = LocalContext.current,
                        rating = (book.subInfo?.ratingInfo?.ratingScore ?: "0").toFloat(),
                        totalCnt = 10
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = horizontalPadding
                    ) {
                        BasicButton(
                            onClick = offStoreInfo,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = stringResource(id = R.string.str_btn_shop_inventory),
                                modifier = Modifier.fillMaxWidth(),
                                color = BookDiaryTheme.colors.textSecondary,
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        BasicButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = stringResource(id = R.string.str_btn_link),
                                modifier = Modifier.fillMaxWidth(),
                                color = BookDiaryTheme.colors.textSecondary,
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    BookDiaryDivider()
                    Text(
                        text = stringResource(id = R.string.str_card_review),
                        style = MaterialTheme.typography.titleLarge,
                        color = BookDiaryTheme.colors.textHelp,
                        modifier = horizontalPadding
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(
                        modifier = horizontalPadding
                    ) {
                        book.subInfo?.cardReviewImgList?.let { list ->
                            items(list) { url ->
                                BookImageCard(
                                    imageUrl = url,
                                    modifier = Modifier
                                        .padding(
                                            start = 4.dp,
                                            end = 4.dp
                                        ),
                                    imageModifier = Modifier
                                        .fillMaxWidth()
                                        .height(360.dp)
                                )
                            }
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .padding(bottom = bottomBarHeight)
                            .navigationBarsPadding()
                            .height(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailSubInfoRow(
    modifier: Modifier = Modifier,
    page: String?,
    stockStatus: String?,
    ratingCnt: String?
) {
    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                color = BookDiaryTheme.colors.uiBackground,
                shape = RoundedCornerShape(10.dp)
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DetailInfoColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            textAlign = TextAlign.Center,
            title = stringResource(id = R.string.str_page),
            detail = page,
            exceptionDetail = stringResource(id = R.string.str_no_page)
        )

        DetailInfoColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            textAlign = TextAlign.Center,
            title = stringResource(id = R.string.str_stock_title),
            detail = if (stockStatus?.isEmpty() == true) stringResource(id = R.string.str_stock) else stockStatus,
            exceptionDetail = stringResource(id = R.string.str_stock)
        )

        DetailInfoColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            textAlign = TextAlign.Center,
            title = stringResource(id = R.string.str_review_cnt),
            detail = ratingCnt,
            exceptionDetail = "0"
        )
    }
}

@Composable
private fun DetailInfoColumn(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    textAlign: TextAlign = TextAlign.Start,
    title: String,
    detail: String?,
    exceptionDetail: String
) {
    Column(
        modifier = modifier
            .padding(bottom = 7.dp),
        horizontalAlignment = horizontalAlignment
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            textAlign = textAlign,
            color = BookDiaryTheme.colors.textHelp,
            modifier = horizontalPadding
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = detail ?: exceptionDetail,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = textAlign,
            color = BookDiaryTheme.colors.textPrimary,
            modifier = horizontalPadding
        )
    }
}

@Composable
private fun DetailBottomBar(
    modifier: Modifier = Modifier,
    insertMyBook: () -> Unit,
    insertWishBook: () -> Unit
) {
    BookDiarySurface(modifier) {
        Column {
            BookDiaryDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .navigationBarsPadding()
                    .then(horizontalPadding)
                    .height(56.dp)
            ) {
                IconButton(
                    onClick = insertWishBook,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = BookDiaryTheme.colors.interactivePrimary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_bookmark_border_24),
                        tint = BookDiaryTheme.colors.iconInteractive,
                        contentDescription = "insert_wish"
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                BasicButton(
                    onClick = insertMyBook,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
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

@Preview(showBackground = true)
@Composable
private fun BookDetailPreview() {
    val dummyBook = BookModel(
        itemId = 123,
        title = "테스트 도서 제목",
        author = "홍길동",
        cover = "",
        priceStandard = "15,000원",
        description = "이 책은 테스트용 설명이 포함되어 있습니다. 상세 정보 미리보기 확인용입니다.",
        categoryName = "소설 > 한국소설",
        publisher = "테스트 출판사",
        pubDate = "2024-01-01",
        subInfo = null,
        link = null,
        mileage = null,
        stockStatus = null,
        categoryId = null,
        salesPoint = null,
        adult = false,
        fixedPrice = true,
        customerReviewRank = "9",
        priceSales = "13,500원",
        mallType = null,
        isbn = null,
        isbn13 = null
    )

    BookDiaryTheme {
        BookDetailContent(
            uiState = BookDetailUiState(
                bookDetail = BookListModel(baseModel = lee.project.domain.book.model.BaseModel(), bookList = listOf(dummyBook)),
                isLoading = false
            ),
            upPress = {},
            onOffStoreInfo = {},
            onWishBook = {},
            onMyBook = {}
        )
    }
}
