package pm.bam.mbc.remote.datasources

import pm.bam.mbc.remote.models.RemotePodcast
import pm.bam.mbc.remote.models.RemotePodcastEpisode

interface RemotePodcastDataSource {

    fun getAllPodcasts(): List<RemotePodcast>

    fun getAllPodcastEpisodes(): List<RemotePodcastEpisode>

}