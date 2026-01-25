package lee.project.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import lee.project.presentation.navigation.MainDestinations
import lee.project.presentation.navigation.bookDiaryNavGraph
import lee.project.presentation.navigation.rememberBookDiaryNavController
import lee.project.presentation.theme.BookDiaryTheme

@Composable
fun MainCompose() {
    BookDiaryTheme {
        val bookDiaryNavController = rememberBookDiaryNavController()
        NavHost(
            navController = bookDiaryNavController.navController,
            startDestination = MainDestinations.HOME_ROUTE
        ) {
            bookDiaryNavGraph(
                onBookSelected = { id, from ->
                    bookDiaryNavController.navigateToBookDetail(id, from)
                },
                onMyBookSelected = { id, from ->
                    bookDiaryNavController.navigateToMyBookDetail(id, from)
                },
                onListSelected = bookDiaryNavController::navigateToRecommendList,
                onManualClick = bookDiaryNavController::navigateToManual,
                onSettingClick = bookDiaryNavController::navigateToSetting,
                upPress = bookDiaryNavController::upPress,
                onNavigateToRoute = bookDiaryNavController::navigateToBottomBarRoute
            )
        }
    }
}
