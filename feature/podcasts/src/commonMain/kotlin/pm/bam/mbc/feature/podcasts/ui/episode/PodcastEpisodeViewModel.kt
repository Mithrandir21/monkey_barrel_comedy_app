package pm.bam.mbc.feature.podcasts.ui.episode

import androidx.lifecycle.ViewModel
import pm.bam.mbc.domain.repositories.artist.ArtistRepository
import pm.bam.mbc.domain.repositories.podcast.PodcastRepository
import pm.bam.mbc.logging.Logger


internal class PodcastEpisodeViewModel(
    private val logger: Logger,
    private val podcastRepository: PodcastRepository,
    private val artistRepository: ArtistRepository
) : ViewModel() {

}