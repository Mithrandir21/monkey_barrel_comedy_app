package pm.bam.mbc.remote.datasources

import pm.bam.mbc.remote.models.RemotePodcast
import pm.bam.mbc.remote.models.RemotePodcastEpisode

interface RemotePodcastDataSource {

    suspend fun getAllPodcasts(): List<RemotePodcast>

    suspend fun getAllPodcastEpisodes(): List<RemotePodcastEpisode>

}