package pm.bam.mbc.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import pm.bam.mbc.feature.artists.navigation.artistScreen
import pm.bam.mbc.feature.artists.navigation.artistsScreen
import pm.bam.mbc.feature.blogs.navigation.blogScreen
import pm.bam.mbc.feature.blogs.navigation.blogPostScreen
import pm.bam.mbc.feature.home.navigation.homeScreen
import pm.bam.mbc.feature.podcasts.navigation.podcastEpisodeScreen
import pm.bam.mbc.feature.podcasts.navigation.podcastScreen
import pm.bam.mbc.feature.podcasts.navigation.podcastsScreen
import pm.bam.mbc.feature.shows.navigation.showScreen
import pm.bam.mbc.feature.shows.navigation.showsScreen
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
            goToNewsItem = { navActions.navigateToNewsItem(it) },
            goToNews = { navActions.navigateToNews() },
            goToShow = { navActions.navigateToShow(it) },
            goToShows = { navActions.navigateToShows() },
            goToArtists = { navActions.navigateToArtists() },
            goToPodcasts = { navActions.navigateToPodcasts() },
            goToBlog = { navActions.navigateToBlog() }
        )

        showScreen(
            navController = navController,
            route = NavigationDestinations.SHOW_ROUTE,
            showIdArg = NavigationDestinationsArgs.SHOW_ID_ARG,
            goToArtists = { navActions.navigateToArtist(it) },
            goToWeb = { url: String, title: String -> navActions.navigateToWeb(url, title) }
        )

        showsScreen(
            navController = navController,
            route = NavigationDestinations.SHOWS_ROUTE,
            goToShow = { navActions.navigateToShow(it) },
        )

        blogScreen(
            navController = navController,
            route = NavigationDestinations.BLOG_ROUTE,
            onViewBlogPost = { navActions.navigateToBlogPost(it) }
        )

        blogPostScreen(
            navController = navController,
            route = NavigationDestinations.BLOG_POST_ROUTE,
            blogPostIdArg = NavigationDestinationsArgs.BLOG_ID_ARG
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

        podcastsScreen(
            navController = navController,
            route = NavigationDestinations.PODCASTS_ROUTE,
            onViewPodcast = { id, title -> navActions.navigateToPodcast(id, title) }
        )

        podcastScreen(
            navController = navController,
            route = NavigationDestinations.PODCAST_ROUTE,
            podcastIdArg = NavigationDestinationsArgs.PODCAST_ID_ARG,
            podcastHeaderTitleArg = NavigationDestinationsArgs.PODCAST_HEADER_TITLE_ARG,
            onViewPodcastEpisode = { podcastEpisodeId -> navActions.navigateToPodcastEpisode(podcastEpisodeId) }
        )

        podcastEpisodeScreen(
            navController = navController,
            route = NavigationDestinations.PODCAST_EPISODE_ROUTE,
            podcastEpisodeIdArg = NavigationDestinationsArgs.PODCAST_EPISODE_ID_ARG,
            onViewShow = { navActions.navigateToShow(it) },
            onViewArtist = { navActions.navigateToArtist(it) }
        )
    }
}