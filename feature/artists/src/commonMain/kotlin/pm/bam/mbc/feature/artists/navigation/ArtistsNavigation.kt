package pm.bam.mbc.feature.artists.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pm.bam.mbc.feature.artists.ui.ArtistsScreen

fun NavGraphBuilder.artistsScreen(
    navController: NavController,
    route: String,
    onViewArtist: (artistId: Long) -> Unit
) {
    composable(route) {
        ArtistsScreen(
            onBack = { navController.popBackStack() },
            onViewArtist = onViewArtist
        )
    }
}