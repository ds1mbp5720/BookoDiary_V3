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
import lee.project.presentation.record.Record
import lee.project.presentation.record.RecordDetailScreen
import lee.project.presentation.search.Search
import lee.project.presentation.theme.BookDiaryTheme
import java.util.Locale

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
    composable(MainSections.RECORD.route) { from ->
        Record(
            onWishBookClick = { id -> onBookSelected(id, from) },
            onMyBookClick = { id -> onMyBookSelected(id, from) },
            onNavigateToRoute = onNavigateToRoute,
            modifier = modifier
        )
    }
    /*composable(MainSections.AppInfo.route) { from ->
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
        arguments = listOf(navArgument(MainDestinations.MY_BOOK_ID_KEY) { type = NavType.LongType })
    ) { backStackEntry ->
        val arguments = requireNotNull(backStackEntry.arguments)
        val myBookId = arguments.getLong(MainDestinations.MY_BOOK_ID_KEY)
        RecordDetailScreen(bookId = myBookId, upPress = upPress)
    }
    // home 화면 한개 리스트 화면
    composable(
        "${MainDestinations.BOOK_LIST_ROOT}/{${MainDestinations.BOOK_LIST_TYPE}}",
        arguments = listOf(navArgument(MainDestinations.BOOK_LIST_TYPE) {
            type = NavType.StringType
        })
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