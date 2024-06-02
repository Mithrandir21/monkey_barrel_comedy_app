package pm.bam.mbc.navigation

import androidx.navigation.NavHostController
import pm.bam.mbc.navigation.NavigationDestinations.ARTISTS_ROUTE
import pm.bam.mbc.navigation.NavigationDestinations.HOME_SCREEN_ROUTE
import pm.bam.mbc.navigation.NavigationDestinations.PODCASTS_ROUTE
import pm.bam.mbc.navigation.NavigationDestinationsArgs.ARTIST_ID_ARG
import pm.bam.mbc.navigation.NavigationDestinationsArgs.PODCAST_EPISODE_ID_ARG
import pm.bam.mbc.navigation.NavigationDestinationsArgs.PODCAST_ID_ARG
import pm.bam.mbc.navigation.NavigationDestinationsArgs.SHOW_ID_ARG
import pm.bam.mbc.navigation.NavigationDestinationsArgs.WEB_TITLE_ARG
import pm.bam.mbc.navigation.NavigationDestinationsArgs.WEB_URL_ARG
import pm.bam.mbc.navigation.NavigationScreens.ARTISTS_SCREEN
import pm.bam.mbc.navigation.NavigationScreens.ARTIST_SCREEN
import pm.bam.mbc.navigation.NavigationScreens.HOME_SCREEN
import pm.bam.mbc.navigation.NavigationScreens.PODCASTS_SCREEN
import pm.bam.mbc.navigation.NavigationScreens.PODCAST_EPISODE_SCREEN
import pm.bam.mbc.navigation.NavigationScreens.PODCAST_SCREEN
import pm.bam.mbc.navigation.NavigationScreens.SHOW_SCREEN
import pm.bam.mbc.navigation.NavigationScreens.WEBVIEW_SCREEN


/** Screens used in [NavigationDestinations]. */
private object NavigationScreens {
    const val HOME_SCREEN = "home"
    const val SHOW_SCREEN = "show"
    const val WEBVIEW_SCREEN = "webview"
    const val ARTISTS_SCREEN = "artists"
    const val ARTIST_SCREEN = "artist"
    const val PODCASTS_SCREEN = "podcasts"
    const val PODCAST_SCREEN = "podcast"
    const val PODCAST_EPISODE_SCREEN = "podcastEpisode"
}

/** Arguments used in [NavigationDestinations] routes. */
internal object NavigationDestinationsArgs {
    const val SHOW_ID_ARG = "showId"
    const val ARTIST_ID_ARG = "artistId"
    const val PODCAST_ID_ARG = "podcastId"
    const val PODCAST_EPISODE_ID_ARG = "podcastEpisodeId"

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
    const val PODCASTS_ROUTE = PODCASTS_SCREEN
    const val PODCAST_ROUTE = "$PODCAST_SCREEN?$PODCAST_ID_ARG={$PODCAST_ID_ARG}"
    const val PODCAST_EPISODE_ROUTE = "$PODCAST_EPISODE_SCREEN?$PODCAST_ID_ARG={$PODCAST_ID_ARG}&$PODCAST_EPISODE_ID_ARG={$PODCAST_EPISODE_ID_ARG}"
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

    fun navigateToPodcasts() {
        navController.navigate(PODCASTS_ROUTE) {
            restoreState = true
        }
    }

    fun navigateToPodcast(podcastId: Long) {
        navController.navigate("$PODCAST_SCREEN?$PODCAST_ID_ARG=${podcastId}") {
            restoreState = true
        }
    }

    fun navigateToPodcastEpisode(podcastId: Long, podcastEpisodeId: Long) {
        navController.navigate("$PODCAST_EPISODE_SCREEN?$PODCAST_ID_ARG=${podcastId}&$PODCAST_EPISODE_ID_ARG=${podcastEpisodeId}") {
            restoreState = true
        }
    }

}
