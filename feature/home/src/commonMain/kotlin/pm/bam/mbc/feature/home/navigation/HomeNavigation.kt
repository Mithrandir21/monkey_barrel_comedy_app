package pm.bam.mbc.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pm.bam.mbc.feature.home.ui.HomeScreen

fun NavGraphBuilder.homeScreen(
    route: String,
    goToShow: (showId: Long) -> Unit,
    goToArtists: () -> Unit
) {
    composable(route) {
        HomeScreen(
            onViewShow = goToShow,
            goToArtists = goToArtists
        )
    }
}