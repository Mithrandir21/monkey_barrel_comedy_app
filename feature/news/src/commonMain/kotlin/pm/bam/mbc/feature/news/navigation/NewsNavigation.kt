package pm.bam.mbc.feature.news.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import pm.bam.mbc.feature.news.ui.item.NewsItemScreen
import pm.bam.mbc.feature.news.ui.news.NewsScreen

fun NavGraphBuilder.newsScreen(
    navController: NavController,
    route: String,
    onViewNewsItem: (newsItemId: Long) -> Unit
) {
    composable(route) {
        NewsScreen(
            onBack = { navController.popBackStack() },
            onViewNewsItem = onViewNewsItem
        )
    }
}


fun NavGraphBuilder.newsItemScreen(
    navController: NavController,
    route: String,
    newsItemIdArg: String,
    onViewShow: (showId: Long) -> Unit,
    goToWeb: (url: String, title: String) -> Unit
) {
    composable(
        route = route,
        arguments = listOf(navArgument(newsItemIdArg) { type = NavType.LongType })
    ) { entry ->
        NewsItemScreen(
            artistId = entry.arguments?.getLong(newsItemIdArg)!!,
            onBack = { navController.popBackStack() },
            onViewShow = onViewShow,
            goToWeb = goToWeb
        )
    }
}