package pm.bam.mbc.feature.podcasts.ui.podcast

import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun PodcastScreen(
    podcastId: Long,
    onBack: () -> Unit,
    onViewPodcastEpisode: (podcast: Long, podcastEpisodeId: Long) -> Unit,
    viewModel: PodcastViewModel = koinViewModel<PodcastViewModel>()
) {

}