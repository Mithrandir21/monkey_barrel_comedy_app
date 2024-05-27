package pm.bam.mbc.domain.repositories.podcast

import kotlinx.coroutines.flow.Flow
import pm.bam.mbc.domain.models.PodcastEpisode

interface PodcastRepository {

    fun observeEpisodes(): Flow<List<PodcastEpisode>>

    fun getEpisode(episodeId: Long): PodcastEpisode

    fun refreshEpisodes()

}