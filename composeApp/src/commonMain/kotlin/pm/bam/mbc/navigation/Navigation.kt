package pm.bam.mbc.navigation

import androidx.navigation.NavHostController
import pm.bam.mbc.navigation.NavigationDestinations.ARTISTS_ROUTE
import pm.bam.mbc.navigation.NavigationDestinations.HOME_SCREEN_ROUTE
import pm.bam.mbc.navigation.NavigationDestinationsArgs.ARTIST_ID_ARG
import pm.bam.mbc.navigation.NavigationDestinationsArgs.SHOW_ID_ARG
import pm.bam.mbc.navigation.NavigationDestinationsArgs.WEB_TITLE_ARG
import pm.bam.mbc.navigation.NavigationDestinationsArgs.WEB_URL_ARG
import pm.bam.mbc.navigation.NavigationScreens.ARTISTS_SCREEN
import pm.bam.mbc.navigation.NavigationScreens.ARTIST_SCREEN
import pm.bam.mbc.navigation.NavigationScreens.HOME_SCREEN
import pm.bam.mbc.navigation.NavigationScreens.SHOW_SCREEN
import pm.bam.mbc.navigation.NavigationScreens.WEBVIEW_SCREEN


/** Screens used in [NavigationDestinations]. */
private object NavigationScreens {
    const val HOME_SCREEN = "home"
    const val SHOW_SCREEN = "show"
    const val WEBVIEW_SCREEN = "webview"
    const val ARTISTS_SCREEN = "artists"
    const val ARTIST_SCREEN = "artist"
}

/** Arguments used in [NavigationDestinations] routes. */
internal object NavigationDestinationsArgs {
    const val SHOW_ID_ARG = "showId"
    const val ARTIST_ID_ARG = "artistId"

    const val WEB_URL_ARG = "url"
    const val WEB_TITLE_ARG = "title"
}


/** Possible destinations used in this Navigation graph. */
internal object NavigationDestinations {
    const val HOME_SCREEN_ROUTE = HOME_SCREEN
    const val SHOW_ROUTE = "$SHOW_SCREEN?$SHOW_ID_ARG={$SHOW_ID_ARG}"
    const val WEBVIEW_ROUTE = "$WEBVIEW_SCREEN?$WEB_URL_ARG={$WEB_URL_ARG}&$WEB_TITLE_ARG={$WEB_TITLE_ARG}"
    const val ARTISTS_ROUTE = ARTISTS_SCREEN
    const val ARTIST_ROUTE = "$ARTIST_SCREEN?$ARTIST_ID_ARG={$ARTIST_ID_ARG}"
}


/**
 * Models the navigation actions in the app.
 */
internal class NavigationActions(private val navController: NavHostController) {

    fun navigateHome() {
        navController.navigate(HOME_SCREEN_ROUTE) {
            // Pop up to the start destination of the graph to avoid building up a large stack of destinations on the back stack as users select items
            popUpTo(HOME_SCREEN_ROUTE) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when re-selecting the same item
            launchSingleTop = true
            // Reshow state when re-selecting a previously selected item
            restoreState = true
        }
    }

    fun navigateToShow(showId: Long) {
        navController.navigate("$SHOW_SCREEN?$SHOW_ID_ARG=${showId}") {
            restoreState = showId == 0.toLong()
        }
    }

    fun navigateToWeb(url: String, title: String) {
        navController.navigate("$WEBVIEW_SCREEN?$WEB_URL_ARG=${url}&$WEB_TITLE_ARG=${title}") {
            restoreState = true
        }
    }

    fun navigateToArtists() {
        navController.navigate(ARTISTS_ROUTE) {
            restoreState = true
        }
    }

    fun navigateToArtist(artistId: Long) {
        navController.navigate("$ARTIST_SCREEN?$ARTIST_ID_ARG=${artistId}") {
            restoreState = true
        }
    }

}
