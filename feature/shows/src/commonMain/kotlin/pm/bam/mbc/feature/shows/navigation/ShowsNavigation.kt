package pm.bam.mbc.feature.shows.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pm.bam.mbc.feature.shows.ui.ShowsScreen

fun NavGraphBuilder.showScreen(
    navController: NavController,
    route: String
) {
    composable(route) {
        ShowsScreen()
    }
}