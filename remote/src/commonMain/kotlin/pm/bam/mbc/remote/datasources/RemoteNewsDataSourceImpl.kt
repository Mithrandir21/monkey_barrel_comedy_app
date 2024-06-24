package pm.bam.mbc.remote.datasources

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.debug
import pm.bam.mbc.logging.verbose
import pm.bam.mbc.remote.models.EPISODE_IDS
import pm.bam.mbc.remote.models.LINKS
import pm.bam.mbc.remote.models.MERCH_IDS
import pm.bam.mbc.remote.models.RemoteNews
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
            .decodeList<RemoteNews>()
            .also { debug(logger) { "Remote DB News decoded Successfully" } }
            .also { verbose(logger) { "Remote DB News: $it" } }

}