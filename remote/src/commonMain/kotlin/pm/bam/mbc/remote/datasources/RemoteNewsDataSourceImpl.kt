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
import pm.bam.mbc.remote.models.RemoteLink
import pm.bam.mbc.remote.models.RemoteNews
import pm.bam.mbc.remote.models.RemoteNewsType
import pm.bam.mbc.remote.models.SHOWS_IDS

internal class RemoteNewsDataSourceImpl(
    private val logger: Logger,
    private val supabaseClient: SupabaseClient
) : RemoteNewsDataSource {

    override suspend fun getAllNews(): List<RemoteNews> =
        supabaseClient.postgrest["news"]
            .also { debug(logger) { "Fetching all Remote DB news" } }
            .select(
                Columns.raw(
                    value = "id, " +
                            "title, " +
                            "description, " +
                            "images, " +
                            "types, " +
                            "$SHOWS_IDS, " +
                            "$MERCH_IDS, " +
                            "$EPISODE_IDS, " +
                            LINKS
                )
            )
            .also { debug(logger) { "Remote DB News fetched Successfully" } }
            .decodeList<RemoteDatabaseNews>()
            .also { debug(logger) { "Remote DB News decoded Successfully" } }
            .also { verbose(logger) { "Remote DB News: $it" } }
            .map { remoteNews ->
                RemoteNews(
                    id = remoteNews.id,
                    title = remoteNews.title,
                    description = remoteNews.description,
                    images = remoteNews.images,
                    types = remoteNews.types,
                    showIds = remoteNews.showIds.orEmpty().map { it.id }.ifEmpty { null },
                    merchIds = remoteNews.merchIds.orEmpty().map { it.id }.ifEmpty { null },
                    episodeIds = remoteNews.episodeIds.orEmpty().map { it.id }.ifEmpty { null },
                    blogPostsIds = remoteNews.blogPostsIds,
                    externalLinks = remoteNews.links
                )
            }
            .also { debug(logger) { "Remote DB News mapped Successfully" } }

}


@Serializable
private data class RemoteDatabaseNews(
    val id: Long,
    val title: String,
    val description: String,
    val images: List<String>,
    val types: List<RemoteNewsType>,
    @SerialName("show_ids")
    val showIds: List<IDsWrapper>? = null,
    @SerialName("merch_ids")
    val merchIds: List<IDsWrapper>? = null,
    @SerialName("episode_ids")
    val episodeIds: List<IDsWrapper>? = null,
    val blogPostsIds: List<Long>? = null,
    val links: List<RemoteLink>? = null
)