package pm.bam.mbc.remote.datasources

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.debug
import pm.bam.mbc.logging.verbose
import pm.bam.mbc.remote.models.ARTIST_IDS
import pm.bam.mbc.remote.models.LINKS
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
            .decodeList<RemotePodcast>()
            .also { debug(logger) { "Remote DB Podcasts decoded Successfully" } }
            .also { verbose(logger) { "Remote DB Podcasts: $it" } }

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
            .decodeList<RemotePodcastEpisode>()
            .also { debug(logger) { "Remote DB Podcast Episodes decoded Successfully" } }
            .also { verbose(logger) { "Remote DB Podcast Episodes: $it" } }
}