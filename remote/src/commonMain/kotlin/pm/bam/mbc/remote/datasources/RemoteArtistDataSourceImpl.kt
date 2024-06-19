package pm.bam.mbc.remote.datasources

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.debug
import pm.bam.mbc.logging.verbose
import pm.bam.mbc.remote.models.EPISODE_IDS
import pm.bam.mbc.remote.models.IDsWrapper
import pm.bam.mbc.remote.models.LINKS
import pm.bam.mbc.remote.models.MERCH_IDS
import pm.bam.mbc.remote.models.RemoteArtist
import pm.bam.mbc.remote.models.RemoteCategories
import pm.bam.mbc.remote.models.RemoteLink
import pm.bam.mbc.remote.models.SHOWS_IDS

class RemoteArtistDataSourceImpl(
    private val logger: Logger,
    private val supabaseClient: SupabaseClient
) : RemoteArtistDataSource {

    override suspend fun getAllArtists(): List<RemoteArtist> =
        supabaseClient.postgrest["artist"]
            .also { debug(logger) { "Fetching all Remote DB artists" } }
            .select(
                Columns.raw(
                    value = "id, " +
                            "firstname, " +
                            "lastname, " +
                            "description, " +
                            "images, " +
                            "genres, " +
                            "$SHOWS_IDS, " +
                            "$MERCH_IDS, " +
                            "$EPISODE_IDS, " +
                            LINKS
                )
            )
            .also { debug(logger) { "Remote DB Artists fetched Successfully" } }
            .decodeList<RemoteDatabaseArtist>()
            .also { debug(logger) { "Remote DB Artists decoded Successfully" } }
            .also { verbose(logger) { "Remote DB Artists: $it" } }
            .map { remoteDatabaseArtist ->
                RemoteArtist(
                    id = remoteDatabaseArtist.id,
                    firstname = remoteDatabaseArtist.firstname,
                    lastname = remoteDatabaseArtist.lastname,
                    description = remoteDatabaseArtist.description,
                    images = remoteDatabaseArtist.images,
                    genres = remoteDatabaseArtist.genres,
                    showsIds = remoteDatabaseArtist.showsIds.orEmpty().map { it.id }.ifEmpty { null },
                    merchIds = remoteDatabaseArtist.merchIds.orEmpty().map { it.id }.ifEmpty { null },
                    podcastsEpisodeIds = remoteDatabaseArtist.podcastsEpisodeIds.orEmpty().map { it.id }.ifEmpty { null },
                    externalLinks = remoteDatabaseArtist.links
                )
            }
            .also { debug(logger) { "Remote DB Artists mapped Successfully" } }

}


@Serializable
private data class RemoteDatabaseArtist(
    val id: Long,
    val firstname: String,
    val lastname: String? = null,
    val description: String,
    val images: List<String>,
    val genres: List<RemoteCategories>,
    @SerialName("show_ids")
    val showsIds: List<IDsWrapper>? = null,
    @SerialName("merch_ids")
    val merchIds: List<IDsWrapper>? = null,
    @SerialName("episode_ids")
    val podcastsEpisodeIds: List<IDsWrapper>? = null,
    val links: List<RemoteLink>? = null
)