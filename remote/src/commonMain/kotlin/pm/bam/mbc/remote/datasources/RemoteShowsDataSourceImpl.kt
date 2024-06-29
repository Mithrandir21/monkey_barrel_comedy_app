package pm.bam.mbc.remote.datasources

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.debug
import pm.bam.mbc.logging.verbose
import pm.bam.mbc.remote.models.ARTIST_IDS
import pm.bam.mbc.remote.models.MERCH_IDS
import pm.bam.mbc.remote.models.RemoteShow
import pm.bam.mbc.remote.models.RemoteShowSchedule


internal class RemoteShowsDataSourceImpl(
    private val logger: Logger,
    private val supabaseClient: SupabaseClient
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
                            MERCH_IDS
                ).also { verbose(logger) { "Remote DB Shows fetch columns: $it" } }
            )
            .also { debug(logger) { "Remote DB Shows fetched Successfully" } }
            .decodeList<RemoteShow>()
            .also { debug(logger) { "Remote DB Shows decoded Successfully" } }
            .also { verbose(logger) { "Remote DB Shows: $it" } }
            .let { shows ->
                val schedules = getAllSchedules().sortedBy { it.start }

                shows.map { show ->
                    show.copy(schedule = schedules.filter { it.showId == show.id })
                }
            }


    // TODO - Look at Inner Join in getAllShows for this instead of separate fetch
    private suspend fun getAllSchedules(): List<RemoteShowSchedule> =
        supabaseClient.postgrest["show_schedule"]
            .also { debug(logger) { "Fetching all Remote DB show schedules" } }
            .select(
                Columns.raw(
                    value = "id, " +
                            "show_id, " +
                            "status, " +
                            "venue, " +
                            "start, " +
                            "end, " +
                            "status_note, " +
                            ARTIST_IDS
                ).also { verbose(logger) { "Remote DB Show Schedules fetch columns: $it" } }
            )
            .also { debug(logger) { "Remote DB Show Schedules fetched Successfully" } }
            .decodeList<RemoteShowSchedule>()
            .also { debug(logger) { "Remote DB Show Schedules decoded Successfully" } }
            .also { verbose(logger) { "Remote DB Show Schedules: $it" } }
}