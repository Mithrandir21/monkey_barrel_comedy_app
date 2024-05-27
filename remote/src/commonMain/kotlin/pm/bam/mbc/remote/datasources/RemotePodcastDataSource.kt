package pm.bam.mbc.remote.datasources

import pm.bam.mbc.remote.models.RemotePodcastEpisode

interface RemotePodcastDataSource {

    fun getAllPodcastEpisodes(): List<RemotePodcastEpisode>

}