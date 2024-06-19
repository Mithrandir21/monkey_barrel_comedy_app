package pm.bam.mbc.remote.datasources

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.debug
import pm.bam.mbc.logging.verbose
import pm.bam.mbc.remote.models.RemoteMerch
import pm.bam.mbc.remote.models.RemoteMerchItem

internal class RemoteMerchDataSourceImpl(
    private val logger: Logger,
    private val supabaseClient: SupabaseClient
) : RemoteMerchDataSource {

    override suspend fun getAllMerch(): List<RemoteMerch> =
        supabaseClient.postgrest["merch"]
            .also { debug(logger) { "Fetching all Remote DB merch" } }
            .select()
            .also { debug(logger) { "Remote DB Merch fetched Successfully" } }
            .decodeList<RemoteMerch>()
            .also { debug(logger) { "Remote DB Merch decoded Successfully" } }
            .also { verbose(logger) { "Remote DB Merch: $it" } }
            .also { debug(logger) { "Remote DB Merch mapped Successfully" } }

    override suspend fun getAllMerchItem(): List<RemoteMerchItem> =
        supabaseClient.postgrest["merch_item"]
            .also { debug(logger) { "Fetching all Remote DB merch items" } }
            .select()
            .also { debug(logger) { "Remote DB Merch Items fetched Successfully" } }
            .decodeList<RemoteMerchItem>()
            .also { debug(logger) { "Remote DB Merch Items decoded Successfully" } }
            .also { verbose(logger) { "Remote DB Merch Items: $it" } }
            .also { debug(logger) { "Remote DB Merch Items mapped Successfully" } }
}