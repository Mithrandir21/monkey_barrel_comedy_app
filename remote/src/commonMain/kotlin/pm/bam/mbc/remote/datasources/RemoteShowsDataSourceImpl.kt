package pm.bam.mbc.remote.datasources

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.debug
import pm.bam.mbc.logging.verbose
import pm.bam.mbc.remote.datetime.RemoteDateTimeParsing
import pm.bam.mbc.remote.models.ARTIST_IDS
import pm.bam.mbc.remote.models.IDsWrapper
import pm.bam.mbc.remote.models.RemoteCategories
import pm.bam.mbc.remote.models.RemoteEventStatus
import pm.bam.mbc.remote.models.RemoteShow
import pm.bam.mbc.remote.models.RemoteShowSchedule
import pm.bam.mbc.remote.models.RemoteShowVenues
import pm.bam.mbc.remote.models.SCHEDULE

internal class RemoteShowsDataSourceImpl(
    private val logger: Logger,
    private val supabaseClient: SupabaseClient,
    private val remoteDateTimeParsing: RemoteDateTimeParsing
) : RemoteShowsDataSource {

    override suspend fun getAllShows(): List<RemoteShow> =
        supabaseClient.postgrest["show"]
            .also { debug(logger) { "Fetching all Remote DB shows" } }
            .select(
                Columns.raw(
                    value = "id, " +
                            "title, " +
                            "description, " +
                            "ticket_url, " +
                            "images, " +
                            "categories, " +
                            "$ARTIST_IDS, " +
                            SCHEDULE
                ).also { verbose(logger) { "Remote DB Shows fetch columns: $it" } }
            )
            .also { debug(logger) { "Remote DB Shows fetched Successfully" } }
            .decodeList<RemoteDatabaseShow>()
            .also { debug(logger) { "Remote DB Shows decoded Successfully" } }
            .also { verbose(logger) { "Remote DB Shows: $it" } }
            .map { remoteDatabaseShow ->
                RemoteShow(
                    id = remoteDatabaseShow.id,
                    title = remoteDatabaseShow.title,
                    url = remoteDatabaseShow.url,
                    images = remoteDatabaseShow.images,
                    description = remoteDatabaseShow.description,
                    categories = remoteDatabaseShow.categories,
                    artistIds = remoteDatabaseShow.artistIds.orEmpty().map { it.id }.ifEmpty { null },
                    schedule = remoteDatabaseShow.schedule.map {
                        RemoteShowSchedule(
                            id = it.id,
                            status = it.status,
                            venue = it.venue,
                            start = remoteDateTimeParsing.parseRemoteDatabaseDateTime(it.start),
                            end = remoteDateTimeParsing.parseRemoteDatabaseDateTime(it.end)
                        )
                    }
                )
            }
            .also { debug(logger) { "Remote DB Shows mapped Successfully" } }

}


@Serializable
private data class RemoteDatabaseShow(
    val id: Long,
    val title: String,
    val description: String,
    @SerialName("ticket_url")
    val url: String,
    val images: List<String>,
    val categories: List<RemoteCategories>? = null,
    @SerialName("artist_ids")
    val artistIds: List<IDsWrapper>? = null,
    val schedule: List<RemoteDatabaseShowSchedule>
)

@Serializable
data class RemoteDatabaseShowSchedule(
    val id: Long,
    val status: RemoteEventStatus,
    val venue: RemoteShowVenues,
    val start: String,
    val end: String
)