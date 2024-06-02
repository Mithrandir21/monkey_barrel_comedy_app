package pm.bam.mbc.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import pm.bam.mbc.feature.artist.navigation.artistScreen
import pm.bam.mbc.feature.artists.navigation.artistsScreen
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
            goToArtists = { navActions.navigateToArtists() }
        )

        showScreen(
            navController = navController,
            route = NavigationDestinations.SHOW_ROUTE,
            showIdArg = NavigationDestinationsArgs.SHOW_ID_ARG,
            goToArtists = { navActions.navigateToArtist(it) },
            goToWeb = { url: String, title: String -> navActions.navigateToWeb(url, title) }
        )

        webviewScreen(
            navController = navController,
            route = NavigationDestinations.WEBVIEW_ROUTE
        )

        artistsScreen(
            navController = navController,
            route = NavigationDestinations.ARTISTS_ROUTE,
            onViewArtist = { navActions.navigateToArtist(it) }
        )

        artistScreen(
            navController = navController,
            route = NavigationDestinations.ARTIST_ROUTE,
            artistIdArg = NavigationDestinationsArgs.ARTIST_ID_ARG,
            onViewShow = { navActions.navigateToShow(it) },
            goToWeb = { url: String, title: String -> navActions.navigateToWeb(url, title) }
        )
    }
}