package pm.bam.mbc.navigation

import androidx.navigation.NavHostController
import pm.bam.mbc.navigation.NavigationDestinations.ARTISTS_ROUTE
import pm.bam.mbc.navigation.NavigationDestinations.BLOG_ROUTE
import pm.bam.mbc.navigation.NavigationDestinations.HOME_SCREEN_ROUTE
import pm.bam.mbc.navigation.NavigationDestinations.MERCH_ROUTE
import pm.bam.mbc.navigation.NavigationDestinations.NEWS_ROUTE
import pm.bam.mbc.navigation.NavigationDestinations.PODCASTS_ROUTE
import pm.bam.mbc.navigation.NavigationDestinations.SHOWS_ROUTE
import pm.bam.mbc.navigation.NavigationDestinationsArgs.ARTIST_ID_ARG
import pm.bam.mbc.navigation.NavigationDestinationsArgs.BLOG_ID_ARG
import pm.bam.mbc.navigation.NavigationDestinationsArgs.NEWS_ITEM_ID_ARG
import pm.bam.mbc.navigation.NavigationDestinationsArgs.PODCAST_EPISODE_ID_ARG
import pm.bam.mbc.navigation.NavigationDestinationsArgs.PODCAST_HEADER_TITLE_ARG
import pm.bam.mbc.navigation.NavigationDestinationsArgs.PODCAST_ID_ARG
import pm.bam.mbc.navigation.NavigationDestinationsArgs.SHOW_ID_ARG
import pm.bam.mbc.navigation.NavigationDestinationsArgs.WEB_TITLE_ARG
import pm.bam.mbc.navigation.NavigationDestinationsArgs.WEB_URL_ARG
import pm.bam.mbc.navigation.NavigationScreens.ARTISTS_SCREEN
import pm.bam.mbc.navigation.NavigationScreens.ARTIST_SCREEN
import pm.bam.mbc.navigation.NavigationScreens.BLOG_POST_SCREEN
import pm.bam.mbc.navigation.NavigationScreens.BLOG_SCREEN
import pm.bam.mbc.navigation.NavigationScreens.HOME_SCREEN
import pm.bam.mbc.navigation.NavigationScreens.MERCH_SCREEN
import pm.bam.mbc.navigation.NavigationScreens.NEWS_ITEM_SCREEN
import pm.bam.mbc.navigation.NavigationScreens.NEWS_SCREEN
import pm.bam.mbc.navigation.NavigationScreens.PODCASTS_SCREEN
import pm.bam.mbc.navigation.NavigationScreens.PODCAST_EPISODE_SCREEN
import pm.bam.mbc.navigation.NavigationScreens.PODCAST_SCREEN
import pm.bam.mbc.navigation.NavigationScreens.SHOWS_SCREEN
import pm.bam.mbc.navigation.NavigationScreens.SHOW_SCHEDULE_SCREEN
import pm.bam.mbc.navigation.NavigationScreens.SHOW_SCREEN
import pm.bam.mbc.navigation.NavigationScreens.WEBVIEW_SCREEN


/** Screens used in [NavigationDestinations]. */
private object NavigationScreens {
    const val HOME_SCREEN = "home"
    const val NEWS_SCREEN = "news"
    const val NEWS_ITEM_SCREEN = "newsItem"
    const val SHOW_SCREEN = "show"
    const val SHOWS_SCREEN = "shows"
    const val SHOW_SCHEDULE_SCREEN = "showSchedule"
    const val BLOG_SCREEN = "blog"
    const val BLOG_POST_SCREEN = "blogPost"
    const val WEBVIEW_SCREEN = "webview"
    const val ARTISTS_SCREEN = "artists"
    const val ARTIST_SCREEN = "artist"
    const val PODCASTS_SCREEN = "podcasts"
    const val PODCAST_SCREEN = "podcast"
    const val MERCH_SCREEN = "merch"
    const val PODCAST_EPISODE_SCREEN = "podcastEpisode"
}

/** Arguments used in [NavigationDestinations] routes. */
internal object NavigationDestinationsArgs {
    const val NEWS_ITEM_ID_ARG = "newsItemId"
    const val SHOW_ID_ARG = "showId"
    const val BLOG_ID_ARG = "blogId"
    const val ARTIST_ID_ARG = "artistId"
    const val PODCAST_ID_ARG = "podcastId"
    const val PODCAST_HEADER_TITLE_ARG = "podcastHeaderTitle"
    const val PODCAST_EPISODE_ID_ARG = "podcastEpisodeId"

    const val WEB_URL_ARG = "url"
    const val WEB_TITLE_ARG = "title"
}


/** Possible destinations used in this Navigation graph. */
internal object NavigationDestinations {
    const val HOME_SCREEN_ROUTE = HOME_SCREEN
    const val NEWS_ROUTE = NEWS_SCREEN
    const val NEWS_ITEM_ROUTE = "$NEWS_ITEM_SCREEN?$NEWS_ITEM_ID_ARG={$NEWS_ITEM_ID_ARG}"
    const val SHOW_ROUTE = "$SHOW_SCREEN?$SHOW_ID_ARG={$SHOW_ID_ARG}"
    const val SHOWS_ROUTE = SHOWS_SCREEN
    const val SHOW_SCHEDULE_ROUTE = "$SHOW_SCHEDULE_SCREEN?$SHOW_ID_ARG={$SHOW_ID_ARG}"
    const val BLOG_ROUTE = BLOG_SCREEN
    const val BLOG_POST_ROUTE = "$BLOG_POST_SCREEN?$BLOG_ID_ARG={$BLOG_ID_ARG}"
    const val WEBVIEW_ROUTE = "$WEBVIEW_SCREEN?$WEB_URL_ARG={$WEB_URL_ARG}&$WEB_TITLE_ARG={$WEB_TITLE_ARG}"
    const val ARTISTS_ROUTE = ARTISTS_SCREEN
    const val ARTIST_ROUTE = "$ARTIST_SCREEN?$ARTIST_ID_ARG={$ARTIST_ID_ARG}"
    const val PODCASTS_ROUTE = PODCASTS_SCREEN
    const val PODCAST_ROUTE = "$PODCAST_SCREEN?$PODCAST_ID_ARG={$PODCAST_ID_ARG}&$PODCAST_HEADER_TITLE_ARG={$PODCAST_HEADER_TITLE_ARG}"
    const val PODCAST_EPISODE_ROUTE = "$PODCAST_EPISODE_SCREEN?$PODCAST_EPISODE_ID_ARG={$PODCAST_EPISODE_ID_ARG}"
    const val MERCH_ROUTE = MERCH_SCREEN
}


/**
 * Models the navigation actions in the app.
 */
internal class NavigationActions(private val navController: NavHostController) {

    fun navigateToHome() {
        navController.navigate(HOME_SCREEN_ROUTE) {
            // Pop to Start Destination and pop Start Destination as well, closing the app.
            popUpTo(HOME_SCREEN_ROUTE) {
                saveState = true
                // inclusive = true not necessary as this is the start destination
            }
            // Avoid multiple copies of the same destination when re-selecting the same item
            launchSingleTop = true
            // Reshow state when re-selecting a previously selected item
            restoreState = true
        }
    }

    fun navigateToNewsItem(newsId: Long) {
        navController.navigate("$NEWS_ITEM_SCREEN?$NEWS_ITEM_ID_ARG=${newsId}") {
            restoreState = true
        }
    }

    fun navigateToNews() {
        navController.navigate(NEWS_ROUTE) {
            restoreState = true
        }
    }

    fun navigateToShow(showId: Long) {
        navController.navigate("$SHOW_SCREEN?$SHOW_ID_ARG=${showId}") {
            restoreState = true
        }
    }

    fun navigateToShows() {
        navController.navigate(SHOWS_ROUTE) {
            popUpTo(HOME_SCREEN_ROUTE) {
                saveState = true
                inclusive = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToShowSchedule(showId: Long) {
        navController.navigate("$SHOW_SCHEDULE_SCREEN?$SHOW_ID_ARG=${showId}") {
            restoreState = true
        }
    }

    fun navigateToBlog() {
        navController.navigate(BLOG_ROUTE) {
            restoreState = true
        }
    }

    fun navigateToBlogPost(blogPostId: Long) {
        navController.navigate("$BLOG_POST_SCREEN?$BLOG_ID_ARG=${blogPostId}") {
            restoreState = true
        }
    }

    fun navigateToWeb(url: String, title: String) {
        navController.navigate("$WEBVIEW_SCREEN?$WEB_URL_ARG=${url}&$WEB_TITLE_ARG=${title}") {
            restoreState = true
        }
    }

    fun navigateToArtists() {
        navController.navigate(ARTISTS_ROUTE) {
            popUpTo(HOME_SCREEN_ROUTE) {
                saveState = true
                inclusive = true
            }
            launchSingleTop = true
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
            popUpTo(HOME_SCREEN_ROUTE) {
                saveState = true
                inclusive = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToPodcast(podcastId: Long, podcastHeaderTitle: String) {
        navController.navigate("$PODCAST_SCREEN?$PODCAST_ID_ARG=${podcastId}&$PODCAST_HEADER_TITLE_ARG=${podcastHeaderTitle}") {
            restoreState = true
        }
    }

    fun navigateToPodcastEpisode(podcastEpisodeId: Long) {
        navController.navigate("$PODCAST_EPISODE_SCREEN?$PODCAST_EPISODE_ID_ARG=${podcastEpisodeId}") {
            restoreState = true
        }
    }

    fun navigateToMerch() {
        navController.navigate(MERCH_ROUTE) {
            popUpTo(HOME_SCREEN_ROUTE) {
                saveState = true
                inclusive = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

}
