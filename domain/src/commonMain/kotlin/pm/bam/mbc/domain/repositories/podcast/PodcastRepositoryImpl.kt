package pm.bam.mbc.domain.repositories.podcast

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pm.bam.mbc.common.serializer.Serializer
import pm.bam.mbc.domain.db.transformations.toDatabasePodcastEpisode
import pm.bam.mbc.domain.db.transformations.toPodcastEpisode
import pm.bam.mbc.domain.models.PodcastEpisode
import pm.bam.mbc.remote.datasources.RemotePodcastDataSource
import pmbammbcdomain.DatabasePodcastEpisodeQueries

internal class PodcastRepositoryImpl(
    private val serializer: Serializer,
    private val remotePodcastDataSource: RemotePodcastDataSource,
    private val podcastEpisodeQueries: DatabasePodcastEpisodeQueries
) : PodcastRepository {

    override fun observeEpisodes(): Flow<List<PodcastEpisode>> =
        podcastEpisodeQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { databaseEpisodes -> databaseEpisodes.map { it.toPodcastEpisode(serializer) } }

    override fun getEpisode(episodeId: Long): PodcastEpisode =
        podcastEpisodeQueries.selectById(episodeId.toLong())
            .executeAsOne()
            .toPodcastEpisode(serializer)

    override fun refreshEpisodes() =
        remotePodcastDataSource.getAllPodcastEpisodes()
            .map { it.toDatabasePodcastEpisode(serializer) }
            .toList()
            .let { episodes ->
                podcastEpisodeQueries.transaction {
                    podcastEpisodeQueries.deleteAll()
                    episodes.forEach { podcastEpisodeQueries.insert(it) }
                }
            }
}