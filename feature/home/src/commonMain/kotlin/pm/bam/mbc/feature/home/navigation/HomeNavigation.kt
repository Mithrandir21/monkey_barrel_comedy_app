package pm.bam.mbc.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pm.bam.mbc.feature.home.ui.HomeScreen

fun NavGraphBuilder.homeScreen(
    route: String,
    goToNewsItem: (newsId: Long) -> Unit,
    goToNews: () -> Unit,
    goToShow: (showId: Long) -> Unit,
    goToShows: () -> Unit,
    goToArtists: () -> Unit,
    goToPodcasts: () -> Unit,
    goToBlog: () -> Unit
) {
    composable(route) {
        HomeScreen(
            goToNewsItem = goToNewsItem,
            goToNews = goToNews,
            onViewShow = goToShow,
            goToShows = goToShows,
            goToArtists = goToArtists,
            goToPodcasts = goToPodcasts,
            goToBlog = goToBlog
        )
    }
}