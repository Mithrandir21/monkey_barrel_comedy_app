package pm.bam.mbc.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pm.bam.mbc.compose.NavigationBarConfig
import pm.bam.mbc.feature.home.ui.HomeScreen

fun NavGraphBuilder.homeScreen(
    route: String,
    bottomNavConfig: NavigationBarConfig,
    goToNewsItem: (newsId: Long) -> Unit,
    goToArtist: (artistId: Long) -> Unit,
    goToNews: () -> Unit,
    goToShow: (showId: Long) -> Unit,
    goToBlog: () -> Unit
) {
    composable(route) {
        HomeScreen(
            bottomNavConfig = bottomNavConfig,
            goToNewsItem = goToNewsItem,
            onViewArtist = goToArtist,
            goToNews = goToNews,
            onViewShow = goToShow,
            goToBlog = goToBlog
        )
    }
}