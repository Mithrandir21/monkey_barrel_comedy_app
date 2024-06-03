package pm.bam.mbc.domain.repositories.podcast

import kotlinx.coroutines.flow.Flow
import pm.bam.mbc.domain.models.Podcast
import pm.bam.mbc.domain.models.PodcastEpisode

interface PodcastRepository {

    fun observePodcasts(): Flow<List<Podcast>>

    fun getPodcasts(podcastId: Long): Podcast

    fun observeEpisodes(podcastId: Long): Flow<List<PodcastEpisode>>

    fun getEpisode(episodeId: Long): PodcastEpisode

    fun refreshPodcasts()

    fun refreshEpisodes()

}