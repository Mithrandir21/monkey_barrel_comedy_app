package pm.bam.mbc.feature.podcasts.ui.episode

import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun PodcastEpisodeScreen(
    podcastId: Long,
    podcastEpisodeId: Long,
    onBack: () -> Unit,
    viewModel: PodcastEpisodeViewModel = koinViewModel<PodcastEpisodeViewModel>()
) {

}