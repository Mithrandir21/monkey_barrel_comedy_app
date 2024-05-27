package pm.bam.mbc.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class RemoteArtist(
    val id: Int,
    val name: String,
    val description: String,
    val image: List<String>,
    val genre: List<String>,
    val showsIds: List<Int>? = null,
    val podcastsIds: List<Int>? = null,
    val blogPostsIds: List<Int>? = null,
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
