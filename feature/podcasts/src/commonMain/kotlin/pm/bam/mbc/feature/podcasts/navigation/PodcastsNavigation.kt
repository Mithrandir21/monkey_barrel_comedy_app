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
    onViewPodcast: (podcast: Long) -> Unit
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
    onViewPodcastEpisode: (podcast: Long, podcastEpisode: Long) -> Unit
) {
    composable(
        route = route,
        arguments = listOf(navArgument(podcastIdArg) { type = NavType.LongType })
    ) { entry ->
        PodcastScreen(
            podcastId = entry.arguments?.getLong(podcastIdArg)!!,
            onBack = { navController.popBackStack() },
            onViewPodcastEpisode = onViewPodcastEpisode
        )
    }
}


fun NavGraphBuilder.podcastEpisodeScreen(
    navController: NavController,
    route: String,
    podcastIdArg: String,
    podcastEpisodeIdArg: String
) {
    composable(
        route = route,
        arguments = listOf(navArgument(podcastIdArg) { type = NavType.LongType }, navArgument(podcastEpisodeIdArg) { type = NavType.LongType })
    ) { entry ->
        PodcastEpisodeScreen(
            podcastId = entry.arguments?.getLong(podcastIdArg)!!,
            podcastEpisodeId = entry.arguments?.getLong(podcastEpisodeIdArg)!!,
            onBack = { navController.popBackStack() }
        )
    }
}