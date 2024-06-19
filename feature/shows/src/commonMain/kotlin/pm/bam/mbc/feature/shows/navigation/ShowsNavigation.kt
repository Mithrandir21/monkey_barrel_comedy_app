package pm.bam.mbc.feature.shows.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import pm.bam.mbc.compose.NavigationBarConfig
import pm.bam.mbc.feature.shows.ui.schedule.ScheduleScreen
import pm.bam.mbc.feature.shows.ui.show.ShowScreen
import pm.bam.mbc.feature.shows.ui.shows.ShowsScreen

fun NavGraphBuilder.showScreen(
    navController: NavController,
    route: String,
    showIdArg: String,
    goToArtists: (artistId: Long) -> Unit,
    goToWeb: (url: String, title: String) -> Unit,
    goToSchedules: (showId: Long) -> Unit,
    goToMerch: (merchId: Long) -> Unit
) {
    composable(
        route = route,
        arguments = listOf(navArgument(showIdArg) { type = NavType.LongType })
    ) { entry ->
        ShowScreen(
            showId = entry.arguments?.getLong(showIdArg)!!,
            onBack = { navController.popBackStack() },
            goToArtists = goToArtists,
            goToWeb = goToWeb,
            goToSchedules = goToSchedules,
            goToMerch = goToMerch
        )
    }
}

fun NavGraphBuilder.showsScreen(
    route: String,
    bottomNavConfig: NavigationBarConfig,
    goToShow: (showId: Long) -> Unit
) {
    composable(route) {
        ShowsScreen(
            bottomNavConfig = bottomNavConfig,
            goToShow = goToShow
        )
    }
}

fun NavGraphBuilder.showScheduleScreen(
    navController: NavController,
    route: String,
    showIdArg: String
) {
    composable(
        route = route,
        arguments = listOf(navArgument(showIdArg) { type = NavType.LongType })
    ) { entry ->
        ScheduleScreen(
            showId = entry.arguments?.getLong(showIdArg)!!,
            onBack = { navController.popBackStack() }
        )
    }
}