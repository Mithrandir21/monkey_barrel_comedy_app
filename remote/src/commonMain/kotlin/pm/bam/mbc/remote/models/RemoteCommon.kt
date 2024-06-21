package pm.bam.mbc.remote.models

import kotlinx.serialization.Serializable


// Used to in SupaBase Raw SQL queries for the Many-to-Many associated tables to get the IDs and other values of the associated entities.
// https://supabase.com/docs/reference/kotlin/select
internal const val SHOWS_IDS = "show_ids: show(id)"
internal const val MERCH_IDS = "merch_ids: merch(id)"
internal const val EPISODE_IDS = "episode_ids: episode(id)"
internal const val ARTIST_IDS = "artist_ids: artist(id)"
internal const val SCHEDULE = "schedule: show_schedule(*)"
internal const val LINKS = "links: link(url, type)"

@Serializable
data class IDsWrapper(val id: Long)

fun List<IDsWrapper>?.mapIds(): List<Long>? = this?.map { it.id }?.ifEmpty { null }

@Serializable
data class RemoteLink(
    val url: String,
    val type: RemoteLinkType
)

enum class RemoteLinkType {
    INSTAGRAM,
    FACEBOOK,
    TWITTER,
    YOUTUBE,
    SPOTIFY,
    WEBSITE,
    SOUNDCLOUD
}

@Serializable
enum class RemoteCategories {
    COMEDY,
    STANDUP,
    MUSICAL,
    FRINGE
}