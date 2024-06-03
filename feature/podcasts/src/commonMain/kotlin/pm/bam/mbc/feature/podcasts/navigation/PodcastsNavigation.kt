package pm.bam.mbc.feature.podcasts.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import pm.bam.mbc.feature.podcasts.ui.episode.PodcastEpisodeScreen
import pm.bam.mbc.feature.podcasts.ui.podcast.PodcastScreen
import pm.bam.mbc.feature.podcasts.ui.podcasts.PodcastsScreen


fun NavGraphBuilder.podcastsScreen(
    navController: NavController,
    route: String,
    onViewPodcast: (podcast: Long, title: String) -> Unit
) {
    composable(route) {
        PodcastsScreen(
            onBack = { navController.popBackStack() },
            onViewPodcast = onViewPodcast
        )
    }
}


fun NavGraphBuilder.podcastScreen(
    navController: NavController,
    route: String,
    podcastIdArg: String,
    podcastHeaderTitleArg: String,
    onViewPodcastEpisode: (podcastEpisode: Long) -> Unit
) {
    composable(
        route = route,
        arguments = listOf(navArgument(podcastIdArg) { type = NavType.LongType }, navArgument(podcastHeaderTitleArg) { type = NavType.StringType })
    ) { entry ->
        PodcastScreen(
            podcastId = entry.arguments?.getLong(podcastIdArg)!!,
            headerTitle = entry.arguments?.getString(podcastHeaderTitleArg)!!,
            onBack = { navController.popBackStack() },
            onViewPodcastEpisode = onViewPodcastEpisode
        )
    }
}


fun NavGraphBuilder.podcastEpisodeScreen(
    navController: NavController,
    route: String,
    podcastEpisodeIdArg: String,
    onViewShow: (showId: Long) -> Unit,
    onViewArtist: (artistId: Long) -> Unit,
) {
    composable(
        route = route,
        arguments = listOf(navArgument(podcastEpisodeIdArg) { type = NavType.LongType })
    ) { entry ->
        PodcastEpisodeScreen(
            podcastEpisodeId = entry.arguments?.getLong(podcastEpisodeIdArg)!!,
            onBack = { navController.popBackStack() },
            onViewShow = onViewShow,
            onViewArtist = onViewArtist
        )
    }
}