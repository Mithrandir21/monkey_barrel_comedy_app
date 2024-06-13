package pm.bam.mbc.remote.datasources

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.debug
import pm.bam.mbc.logging.verbose
import pm.bam.mbc.remote.models.ARTIST_IDS
import pm.bam.mbc.remote.models.IDsWrapper
import pm.bam.mbc.remote.models.LINKS
import pm.bam.mbc.remote.models.RemoteLink
import pm.bam.mbc.remote.models.RemotePodcast
import pm.bam.mbc.remote.models.RemotePodcastEpisode
import pm.bam.mbc.remote.models.SHOWS_IDS

class RemotePodcastDataSourceImpl(
    private val logger: Logger,
    private val supabaseClient: SupabaseClient
) : RemotePodcastDataSource {

    override suspend fun getAllPodcasts(): List<RemotePodcast> =
        supabaseClient.postgrest["podcast"]
            .also { debug(logger) { "Fetching all Remote DB podcasts" } }
            .select(
                Columns.raw(
                    value = "id, " +
                            "name, " +
                            "description, " +
                            "images, " +
                            LINKS
                )
            )
            .also { debug(logger) { "Remote DB Podcasts fetched Successfully" } }
            .decodeList<RemoteDatabasePodcast>()
            .also { debug(logger) { "Remote DB Podcasts decoded Successfully" } }
            .also { verbose(logger) { "Remote DB Podcasts: $it" } }
            .map { remoteDatabasePodcast ->
                RemotePodcast(
                    id = remoteDatabasePodcast.id,
                    name = remoteDatabasePodcast.name,
                    description = remoteDatabasePodcast.description,
                    images = remoteDatabasePodcast.images,
                    links = remoteDatabasePodcast.links
                )
            }
            .also { debug(logger) { "Remote DB Podcasts mapped Successfully" } }

    override suspend fun getAllPodcastEpisodes(): List<RemotePodcastEpisode> =
        supabaseClient.postgrest["episode"]
            .also { debug(logger) { "Fetching all Remote DB podcast episodes" } }
            .select(
                Columns.raw(
                    value = "id, " +
                            "name, " +
                            "description, " +
                            "images, " +
                            "duration, " +
                            "release_date, " +
                            "podcast_id, " +
                            "$SHOWS_IDS, " +
                            "$ARTIST_IDS, " +
                            LINKS
                )
            )
            .also { debug(logger) { "Remote DB Podcast Episodes fetched Successfully" } }
            .decodeList<RemoteDatabasePodcastEpisode>()
            .also { debug(logger) { "Remote DB Podcast Episodes decoded Successfully" } }
            .also { verbose(logger) { "Remote DB Podcast Episodes: $it" } }
            .map { remoteDatabasePodcastEpisode ->
                RemotePodcastEpisode(
                    id = remoteDatabasePodcastEpisode.id,
                    name = remoteDatabasePodcastEpisode.name,
                    description = remoteDatabasePodcastEpisode.description,
                    images = remoteDatabasePodcastEpisode.images,
                    links = remoteDatabasePodcastEpisode.links,
                    duration = remoteDatabasePodcastEpisode.duration,
                    releaseDate = remoteDatabasePodcastEpisode.releaseDate,
                    podcastId = remoteDatabasePodcastEpisode.podcastId,
                    showId = remoteDatabasePodcastEpisode.showIds.orEmpty().map { it.id }.ifEmpty { null },
                    artistId = remoteDatabasePodcastEpisode.artistIds.orEmpty().map { it.id }.ifEmpty { null }
                )
            }
            .also { debug(logger) { "Remote DB Podcast Episodes mapped Successfully" } }
}


@Serializable
private data class RemoteDatabasePodcast(
    val id: Long,
    val name: String,
    val description: String,
    val images: List<String>,
    val links: List<RemoteLink>
)


@Serializable
private data class RemoteDatabasePodcastEpisode(
    val id: Long,
    val name: String,
    val description: String,
    val images: List<String>,
    val duration: Long,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("podcast_id")
    val podcastId: Long,
    @SerialName("show_ids")
    val showIds: List<IDsWrapper>? = null,
    @SerialName("artist_ids")
    val artistIds: List<IDsWrapper>? = null,
    val links: List<RemoteLink>,
)