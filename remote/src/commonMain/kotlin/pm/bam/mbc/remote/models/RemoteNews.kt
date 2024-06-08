package pm.bam.mbc.remote.models

import kotlinx.serialization.Serializable


@Serializable
data class RemoteNews(
    val id: Long,
    val title: String,
    val description: String,
    val images: List<String>,
    val types: List<RemoteNewsType>,
    val showsIds: List<Long>? = null,
    val podcastsIds: List<Long>? = null,
    val blogPostsIds: List<Long>? = null,
    val externalLinks: List<RemoteNewsExternalLink>? = null
)

enum class RemoteNewsType {
    COMEDY,
    STANDUP,
    BUSINESS,
    PODCAST
}

@Serializable
data class RemoteNewsExternalLink(
    val url: String,
    val type: RemoteNewsExternalLinkType
)

enum class RemoteNewsExternalLinkType {
    INSTAGRAM,
    FACEBOOK,
    TWITTER,
    YOUTUBE,
    SPOTIFY,
    WEBSITE
}
