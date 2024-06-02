package pm.bam.mbc.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class RemotePodcast(
    val id: Long,
    val name: String,
    val description: String,
    val images: List<String>,
    val links: List<RemotePodcastEpisodeExternalLink>
)

@Serializable
data class RemotePodcastExternalLink(
    val url: String,
    val type: RemotePodcastEpisodeExternalLinkType
)

enum class RemotePodcastExternalLinkType {
    SPOTIFY,
    YOUTUBE,
    SOUNDCLOUD
}
