package pm.bam.mbc.feature.show.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import pm.bam.mbc.feature.show.ui.ShowScreen

fun NavGraphBuilder.showScreen(
    navController: NavController,
    route: String,
    showIdArg: String,
    goToArtists: (artistId: Long) -> Unit,
    goToWeb: (url: String, title: String) -> Unit
) {
    composable(
        route = route,
        arguments = listOf(navArgument(showIdArg) { type = NavType.LongType })
    ) {entry ->
        ShowScreen(
            showId = entry.arguments?.getLong(showIdArg)!!,
            onBack = { navController.popBackStack() },
            goToArtists = goToArtists,
            goToWeb = goToWeb
        )
    }
}