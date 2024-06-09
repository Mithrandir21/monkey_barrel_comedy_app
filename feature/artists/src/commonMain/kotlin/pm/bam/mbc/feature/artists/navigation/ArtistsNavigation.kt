package pm.bam.mbc.feature.artists.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import pm.bam.mbc.compose.NavigationBarConfig
import pm.bam.mbc.feature.artists.ui.artist.ArtistScreen
import pm.bam.mbc.feature.artists.ui.artists.ArtistsScreen

fun NavGraphBuilder.artistsScreen(
    route: String,
    bottomNavConfig: NavigationBarConfig,
    onViewArtist: (artistId: Long) -> Unit
) {
    composable(route) {
        ArtistsScreen(
            bottomNavConfig = bottomNavConfig,
            onViewArtist = onViewArtist
        )
    }
}


fun NavGraphBuilder.artistScreen(
    navController: NavController,
    route: String,
    artistIdArg: String,
    onViewShow: (showId: Long) -> Unit,
    goToWeb: (url: String, title: String) -> Unit
) {
    composable(
        route = route,
        arguments = listOf(navArgument(artistIdArg) { type = NavType.LongType })
    ) { entry ->
        ArtistScreen(
            artistId = entry.arguments?.getLong(artistIdArg)!!,
            onBack = { navController.popBackStack() },
            onViewShow = onViewShow,
            goToWeb = goToWeb
        )
    }
}