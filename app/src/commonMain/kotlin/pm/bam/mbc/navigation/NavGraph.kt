package pm.bam.mbc.navigation

import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import pm.bam.mbc.compose.BottomNavigationDestinations
import pm.bam.mbc.compose.createBottomNavigationConfig
import pm.bam.mbc.feature.artists.navigation.artistScreen
import pm.bam.mbc.feature.artists.navigation.artistsScreen
import pm.bam.mbc.feature.blogs.navigation.blogPostScreen
import pm.bam.mbc.feature.blogs.navigation.blogScreen
import pm.bam.mbc.feature.home.navigation.homeScreen
import pm.bam.mbc.feature.news.navigation.newsItemScreen
import pm.bam.mbc.feature.news.navigation.newsScreen
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
    val homeNavConfig = createNavigationBarConfig(
        selectedType = BottomNavigationDestinations.HOME,
        navActions = navActions
    )
    val artistsNavConfig = createNavigationBarConfig(
        selectedType = BottomNavigationDestinations.ARTISTS,
        navActions = navActions
    )
    val showsNavConfig = createNavigationBarConfig(
        selectedType = BottomNavigationDestinations.SHOWS,
        navActions = navActions
    )
    val podcastsNavConfig = createNavigationBarConfig(
        selectedType = BottomNavigationDestinations.PODCASTS,
        navActions = navActions
    )
    val merchNavConfig = createNavigationBarConfig(
        selectedType = BottomNavigationDestinations.MERCH,
        navActions = navActions
    )

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = { fadeIn(animationSpec = tween(500)) },
        exitTransition = { fadeOut(animationSpec = tween(500)) }
    ) {
        homeScreen(
            route = NavigationDestinations.HOME_SCREEN_ROUTE,
            bottomNavConfig = homeNavConfig,
            goToNewsItem = { navActions.navigateToNewsItem(it) },
            goToArtist = { navActions.navigateToArtist(it) },
            goToNews = { navActions.navigateToNews() },
            goToShow = { navActions.navigateToShow(it) },
            goToBlog = { navActions.navigateToBlog() }
        )

        newsScreen(
            navController = navController,
            route = NavigationDestinations.NEWS_ROUTE,
            onViewNewsItem = { navActions.navigateToNewsItem(it) }
        )

        newsItemScreen(
            navController = navController,
            route = NavigationDestinations.NEWS_ITEM_ROUTE,
            newsItemIdArg = NavigationDestinationsArgs.NEWS_ITEM_ID_ARG,
            onViewShow = { navActions.navigateToShow(it) },
            goToWeb = { url: String, title: String -> navActions.navigateToWeb(url, title) }
        )

        showScreen(
            navController = navController,
            route = NavigationDestinations.SHOW_ROUTE,
            showIdArg = NavigationDestinationsArgs.SHOW_ID_ARG,
            goToArtists = { navActions.navigateToArtist(it) },
            goToWeb = { url: String, title: String -> navActions.navigateToWeb(url, title) }
        )

        showsScreen(
            route = NavigationDestinations.SHOWS_ROUTE,
            bottomNavConfig = showsNavConfig,
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
            route = NavigationDestinations.ARTISTS_ROUTE,
            bottomNavConfig = artistsNavConfig,
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
            route = NavigationDestinations.PODCASTS_ROUTE,
            bottomNavConfig = podcastsNavConfig,
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

@Composable
private fun createNavigationBarConfig(
    selectedType: BottomNavigationDestinations,
    navActions: NavigationActions
) = createBottomNavigationConfig(
    selectedType = selectedType,
    onHomeSelected = { navActions.navigateToHome() },
    onShowsSelected = { navActions.navigateToShows() },
    onArtistsSelected = { navActions.navigateToArtists() },
    onPodcastsSelected = { navActions.navigateToPodcasts() },
    onMerchSelected = { }
)