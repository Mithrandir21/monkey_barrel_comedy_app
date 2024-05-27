package pm.bam.mbc.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class RemoteArtist(
    val id: Long,
    val name: String,
    val description: String,
    val images: List<String>,
    val genres: List<String>,
    val showsIds: List<Long>? = null,
    val podcastsIds: List<Long>? = null,
    val blogPostsIds: List<Long>? = null,
    val externalLinks: List<RemoteArtistExternalLink>? = null
)

@Serializable
data class RemoteArtistExternalLink(
    val url: String,
    val type: RemoteArtistExternalLinkType
)

enum class RemoteArtistExternalLinkType {
    INSTAGRAM,
    FACEBOOK,
    TWITTER,
    YOUTUBE,
    SPOTIFY,
    WEBSITE
}
