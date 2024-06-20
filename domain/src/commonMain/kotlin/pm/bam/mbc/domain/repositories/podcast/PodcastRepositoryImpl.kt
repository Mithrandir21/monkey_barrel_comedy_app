package pm.bam.mbc.domain.repositories.podcast

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pm.bam.mbc.common.serializer.Serializer
import pm.bam.mbc.domain.db.transformations.toDatabasePodcast
import pm.bam.mbc.domain.db.transformations.toDatabasePodcastEpisode
import pm.bam.mbc.domain.db.transformations.toPodcast
import pm.bam.mbc.domain.db.transformations.toPodcastEpisode
import pm.bam.mbc.domain.models.Podcast
import pm.bam.mbc.domain.models.PodcastEpisode
import pm.bam.mbc.remote.datasources.RemotePodcastDataSource
import pmbammbcdomain.DatabasePodcastEpisodeQueries
import pmbammbcdomain.DatabasePodcastQueries

internal class PodcastRepositoryImpl(
    private val serializer: Serializer,
    private val remotePodcastDataSource: RemotePodcastDataSource,
    private val podcastQueries: DatabasePodcastQueries,
    private val podcastEpisodeQueries: DatabasePodcastEpisodeQueries
) : PodcastRepository {

    override fun observePodcasts(): Flow<List<Podcast>> =
        podcastQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { databasePodcast -> databasePodcast.map { it.toPodcast(serializer) } }

    override fun getPodcasts(podcastId: Long): Podcast =
        podcastQueries.selectById(podcastId)
            .executeAsOne()
            .toPodcast(serializer)

    override fun observeEpisodes(podcastId: Long): Flow<List<PodcastEpisode>> =
        podcastEpisodeQueries.selectByPodcastId(podcastId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { databaseEpisodes -> databaseEpisodes.map { it.toPodcastEpisode(serializer) } }

    override fun getEpisode(episodeId: Long): PodcastEpisode =
        podcastEpisodeQueries.selectById(episodeId)
            .executeAsOne()
            .toPodcastEpisode(serializer)

    override fun getEpisodes(vararg episodeId: Long): List<PodcastEpisode> =
        podcastEpisodeQueries.selectByIds(episodeId.toList())
            .executeAsList()
            .map { it.toPodcastEpisode(serializer) }

    override suspend fun refreshPodcasts() =
        remotePodcastDataSource.getAllPodcasts()
            .map { it.toDatabasePodcast(serializer) }
            .toList()
            .let { podcasts ->
                podcastQueries.transaction {
                    podcastQueries.deleteAll()
                    podcasts.forEach { podcastQueries.insert(it) }
                }
            }

    override suspend fun refreshEpisodes() =
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