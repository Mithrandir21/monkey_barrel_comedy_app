package pm.bam.mbc.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import pm.bam.mbc.feature.home.navigation.homeScreen
import pm.bam.mbc.feature.shows.navigation.showScreen
import pm.bam.mbc.feature.webview.navigation.webviewScreen

@Composable
internal fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavigationDestinations.HOME_SCREEN_ROUTE,
    navActions: NavigationActions = remember(navController) { NavigationActions(navController) }
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        homeScreen(
            route = NavigationDestinations.HOME_SCREEN_ROUTE,
            goToShow = { navActions.navigateToShow(it) },
        )

        showScreen(
            navController = navController,
            route = NavigationDestinations.SHOW_ROUTE
        )

        webviewScreen(
            navController = navController,
            route = NavigationDestinations.WEBVIEW_ROUTE
        )
    }
}