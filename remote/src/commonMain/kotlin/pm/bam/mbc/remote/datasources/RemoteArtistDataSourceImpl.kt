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
import pm.bam.mbc.remote.models.RemoteArtist
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
            .decodeList<RemoteArtist>()
            .also { debug(logger) { "Remote DB Artists decoded Successfully" } }
            .also { verbose(logger) { "Remote DB Artists: $it" } }
}