package pm.bam.mbc.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class RemotePodcastEpisode(
    val id: Long,
    val name: String,
    val description: String,
    val images: List<String>,
    val links: List<RemotePodcastEpisodeExternalLink>,
    val duration: String,
    val releaseDate: String,
    val podcastId: Long,
    val showId: Long? = null,
    val artistId: Long? = null
)

@Serializable
data class RemotePodcastEpisodeExternalLink(
    val url: String,
    val type: RemotePodcastEpisodeExternalLinkType
)

enum class RemotePodcastEpisodeExternalLinkType {
    SPOTIFY,
    YOUTUBE,
    SOUNDCLOUD
}
