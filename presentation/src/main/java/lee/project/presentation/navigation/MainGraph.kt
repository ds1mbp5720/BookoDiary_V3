package lee.project.presentation.navigation

import androidx.annotation.FloatRange
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.core.os.ConfigurationCompat
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import lee.project.presentation.R
import lee.project.presentation.component.BookDiarySurface
import lee.project.presentation.home.Home
import lee.project.presentation.home.HomeListType
import lee.project.presentation.home.SingleCategoryListScreen
import lee.project.presentation.detail.BookDetail
import lee.project.presentation.search.Search
import lee.project.presentation.theme.BookDiaryTheme
import java.util.Locale

private val textIconSpacing = 2.dp
private val bottomNavHeight = 56.dp
private val bottomNavLabelTransformOrigin = TransformOrigin(0f, 0.5f)
private val bottomNavIndicatorShape = RoundedCornerShape(percent = 50)
private val bottomNavigationItemPadding = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)

enum class MainSections(
    @StringRes val title: Int,
    val icon: ImageVector,
    val route: String
) {
    HOME(R.string.str_home, Icons.Outlined.Home, "main/home"),
    SEARCH(R.string.str_search, Icons.Outlined.Search, "main/search"),
    RECORD(R.string.str_record, Icons.Outlined.Create, "main/record"),
    AppInfo(R.string.str_app_info, Icons.Outlined.AccountCircle, "main/appInfo")
}

fun NavGraphBuilder.addMainGraph(
    onBookSelected: (Long, NavBackStackEntry) -> Unit,
    onMyBookSelected: (Long, NavBackStackEntry) -> Unit,
    onListSelected: (String, NavBackStackEntry) -> Unit,
    onManualClick: (NavBackStackEntry) -> Unit,
    onSettingClick: (NavBackStackEntry) -> Unit,
    onNavigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    composable(MainSections.HOME.route) { from ->
        Home(
            onBookClick = { id -> onBookSelected(id, from) },
            onMyBookClick = { id ->
                /*recordViewModel.getMyBookList()
                recordViewModel.findMyBook(id)
                onMyBookSelected(id, from)*/
            },
            onListClick = { type -> onListSelected(type, from) },
            onNavigateToRoute = onNavigateToRoute,
            modifier = modifier
        )
    }
    composable(MainSections.SEARCH.route) { from ->
        Search(
            onBookClick = { id -> onBookSelected(id, from) },
            onNavigateToRoute = onNavigateToRoute,
            modifier = modifier,
        )
    }
    /*composable(MainSections.RECORD.route) { from ->
        Record(
            onWishBookClick = { id -> onBookSelected(id, from) },
            onMyBookClick = { id -> onMyBookSelected(id, from) },
            onNavigateToRoute = onNavigateToRoute,
            modifier = modifier,
            viewModel = recordViewModel
        )
    }
    composable(MainSections.AppInfo.route) { from ->
        AppInfo(
            onManualClick = { onManualClick(from) },
            onSettingClick = { onSettingClick(from) },
            onNavigateToRoute = onNavigateToRoute,
            modifier = modifier
        )
    }*/
}

fun NavGraphBuilder.bookDiaryNavGraph(
    onBookSelected: (Long, NavBackStackEntry) -> Unit,
    onMyBookSelected: (Long, NavBackStackEntry) -> Unit,
    onListSelected: (String, NavBackStackEntry) -> Unit,
    onManualClick: (NavBackStackEntry) -> Unit,
    onSettingClick: (NavBackStackEntry) -> Unit,
    upPress: () -> Unit,
    onNavigateToRoute: (String) -> Unit
) {
    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = MainSections.HOME.route,
    ) {
        addMainGraph(
            onBookSelected = onBookSelected,
            onMyBookSelected = onMyBookSelected,
            onListSelected = onListSelected,
            onManualClick = onManualClick,
            onSettingClick = onSettingClick,
            onNavigateToRoute = onNavigateToRoute
        )
    }
    // 책 상세보기 화면
    composable(
        "${MainDestinations.BOOK_DETAIL_ROOT}/{${MainDestinations.BOOK_ID_KEY}}",
        arguments = listOf(navArgument(MainDestinations.BOOK_ID_KEY) { type = NavType.LongType })
    ) { backStackEntry ->
        val arguments = requireNotNull(backStackEntry.arguments)
        val bookId = arguments.getLong(MainDestinations.BOOK_ID_KEY)
        BookDetail(bookId = bookId, upPress = upPress)
    }
    // 책 기록 상세보기 화면
    composable(
        "${MainDestinations.MY_BOOK_DETAIL_ROOT}/{${MainDestinations.MY_BOOK_ID_KEY}}",
        arguments = listOf(navArgument(MainDestinations.MY_BOOK_ID_KEY) {type = NavType.LongType} )
    ) { backStackEntry ->
        val arguments = requireNotNull(backStackEntry.arguments)
        val myBookId = arguments.getLong(MainDestinations.MY_BOOK_ID_KEY)
        //RecordDetailScreen(upPress = upPress, recordViewModel = recordViewModel)
    }
    // home 화면 한개 리스트 화면
    composable(
        "${MainDestinations.BOOK_LIST_ROOT}/{${MainDestinations.BOOK_LIST_TYPE}}",
        arguments = listOf(navArgument(MainDestinations.BOOK_LIST_TYPE) { type = NavType.StringType })
    ) { backStackEntry ->
        val arguments = requireNotNull(backStackEntry.arguments)
        val listType = arguments.getString(MainDestinations.BOOK_LIST_TYPE)
        SingleCategoryListScreen(
            listType = when (listType) {
                HomeListType.ItemNewAll.toString() -> HomeListType.ItemNewAll
                HomeListType.ItemNewSpecial.toString() -> HomeListType.ItemNewSpecial
                HomeListType.Bestseller.toString() -> HomeListType.Bestseller
                HomeListType.BlogBest.toString() -> HomeListType.BlogBest
                else -> HomeListType.ItemNewAll
            },
            onBookClick = { id -> onBookSelected(id, backStackEntry) },
            upPress = upPress
        )
    }
    // 앱 설명 화면
    composable(
        MainDestinations.MANUAL
    ) {
        //ManualViewPager()
    }
    // 환경설정 화면
    composable(
        MainDestinations.SETTING
    ) {
        //SettingScreen()
    }
}

@Composable
fun BookDiaryBottomBar(
    tabs: Array<MainSections>,
    currentRoute: String,
    navigateToRoute: (String) -> Unit,
    color: Color = BookDiaryTheme.colors.iconPrimary,
    contentColor: Color = BookDiaryTheme.colors.iconInteractive
) {
    val routes = remember { tabs.map { it.route } }
    val currentSection = tabs.first { it.route == currentRoute }

    BookDiarySurface(
        color = color,
        contentColor = contentColor
    ) {
        val springSpec = SpringSpec<Float>(
            stiffness = 800f,
            dampingRatio = 0.8f
        )
        BookDiaryBottomNavLayout(
            selectedIndex = currentSection.ordinal,
            itemCount = routes.size,
            animSpec = springSpec,
            indicator = { BookDiaryBottomNavIndicator() },
            modifier = Modifier.navigationBarsPadding()
        ) {
            val configuration = LocalConfiguration.current
            val currentLocale: Locale =
                ConfigurationCompat.getLocales(configuration).get(0) ?: Locale.getDefault()

            tabs.forEach { section ->
                val selected = section == currentSection
                val tint by animateColorAsState(
                    targetValue = if (selected) BookDiaryTheme.colors.iconInteractive else BookDiaryTheme.colors.iconInteractiveInactive,
                    label = ""
                )
                // 하단 메뉴 영문 항목일때 uppercase 사용
                val text = stringResource(id = section.title).uppercase(currentLocale)

                BookDiaryBottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = section.icon,
                            tint = tint,
                            contentDescription = text
                        )
                    },
                    text = {
                        Text(
                            text = text,
                            color = tint,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1
                        )
                    },
                    selected = selected,
                    onSelected = { navigateToRoute(section.route) },
                    animSpec = springSpec,
                    modifier = bottomNavigationItemPadding
                        .clip(bottomNavIndicatorShape)
                )
            }
        }
    }
}

@Composable
fun BookDiaryBottomNavigationItem(
    icon: @Composable BoxScope.() -> Unit,
    text: @Composable BoxScope.() -> Unit,
    selected: Boolean,
    onSelected: () -> Unit,
    animSpec: AnimationSpec<Float>,
    modifier: Modifier = Modifier
) {
    val animationProgress by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = animSpec,
        label = ""
    )
    BookDiaryBottomNavItemLayout(
        icon = icon,
        text = text,
        animateProgress = animationProgress,
        modifier = modifier
            .selectable(
                selected = selected,
                onClick = onSelected
            )
            .wrapContentSize()
    )
}

@Composable
private fun BookDiaryBottomNavItemLayout(
    icon: @Composable BoxScope.() -> Unit,
    text: @Composable BoxScope.() -> Unit,
    @FloatRange(from = 0.0, to = 1.0) animateProgress: Float,
    modifier: Modifier = Modifier
) {
    Layout(
        modifier = modifier,
        content = {
            Box(
                modifier = Modifier
                    .layoutId("icon")
                    .padding(horizontal = textIconSpacing),
                content = icon
            )
            val scale = lerp(0.6f, 1f, animateProgress)
            Box(
                modifier = Modifier
                    .layoutId("text")
                    .padding(horizontal = textIconSpacing)
                    .graphicsLayer {
                        alpha = animateProgress
                        scaleX = scale
                        scaleY = scale
                        transformOrigin = bottomNavLabelTransformOrigin
                    },
                content = text
            )
        }
    ) { measurables, constraints ->
        val iconPlaceable = measurables.first { it.layoutId == "icon" }.measure(constraints)
        val textPlaceable = measurables.first { it.layoutId == "text" }.measure(constraints)

        placeTextAndIcon(
            textPlaceable = textPlaceable,
            iconPlaceable = iconPlaceable,
            width = constraints.maxWidth,
            height = constraints.maxHeight,
            animateProgress = animateProgress
        )
    }
}

private fun MeasureScope.placeTextAndIcon(
    textPlaceable: Placeable,
    iconPlaceable: Placeable,
    width: Int,
    height: Int,
    @FloatRange(from = 0.0, to = 1.0) animateProgress: Float
): MeasureResult {
    val iconY = (height - iconPlaceable.height) / 2
    val textY = (height - iconPlaceable.height) / 2

    val textWidth = textPlaceable.width * animateProgress
    val iconX = (width - textWidth - iconPlaceable.width) / 2
    val textX = iconX + iconPlaceable.width

    return layout(width, height) {
        iconPlaceable.placeRelative(iconX.toInt(), iconY)
        if (animateProgress != 0f) {
            textPlaceable.placeRelative(textX.toInt(), textY)
        }
    }
}

@Composable
private fun BookDiaryBottomNavLayout(
    selectedIndex: Int,
    itemCount: Int,
    animSpec: AnimationSpec<Float>,
    indicator: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // 선택 구분
    val selectionFractions = remember(itemCount) {
        List(itemCount) { i ->
            Animatable(if (i == selectedIndex) 1f else 0f)
        }
    }
    selectionFractions.forEachIndexed { index, animatable ->
        val target = if (index == selectedIndex) 1f else 0f
        LaunchedEffect(target, animSpec) {
            animatable.animateTo(target, animSpec)
        }
    }

    // 위치별 indicator
    val indicatorIndex = remember { Animatable(0f) }
    val targetIndicatorIndex = selectedIndex.toFloat()
    LaunchedEffect(targetIndicatorIndex) {
        indicatorIndex.animateTo(targetIndicatorIndex, animSpec)
    }

    //
    Layout(
        modifier = modifier.height(bottomNavHeight),
        content = {
            content()
            Box(Modifier.layoutId("indicator"), content = indicator)
        }
    ) { measurables, constraints ->
        check(itemCount == (measurables.size - 1))

        val unselectedWidth = constraints.maxWidth / (itemCount + 1)
        val selectedWidth = 2 * unselectedWidth
        val indicatorMeasurable = measurables.first { it.layoutId == "indicator" }

        val itemPlaceables = measurables
            .filterNot { it == indicatorMeasurable }
            .mapIndexed { index, measurable ->
                val width = lerp(unselectedWidth, selectedWidth, selectionFractions[index].value)
                measurable.measure(
                    constraints.copy(
                        minWidth = width,
                        maxWidth = width
                    )
                )
            }
        val indicatorPlaceable = indicatorMeasurable.measure(
            constraints.copy(
                minWidth = selectedWidth,
                maxWidth = selectedWidth
            )
        )

        layout(
            width = constraints.maxWidth,
            height = itemPlaceables.maxByOrNull { it.height }?.height ?: 0
        ) {
            val indicatorLeft = indicatorIndex.value * unselectedWidth
            indicatorPlaceable.placeRelative(x = indicatorLeft.toInt(), y = 0)
            var x = 0
            itemPlaceables.forEach {
                it.placeRelative(x = x, y = 0)
                x += it.width
            }
        }
    }
}

@Composable
private fun BookDiaryBottomNavIndicator(
    strokeWidth: Dp = 2.dp,
    color: Color = BookDiaryTheme.colors.iconInteractive,
    shape: Shape = bottomNavIndicatorShape
) {
    Spacer(
        modifier = Modifier
            .fillMaxSize()
            .then(bottomNavigationItemPadding)
            .border(strokeWidth, color, shape)
    )
}