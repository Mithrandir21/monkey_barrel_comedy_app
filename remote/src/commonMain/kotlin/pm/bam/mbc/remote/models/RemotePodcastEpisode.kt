package pm.bam.mbc.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class RemotePodcastEpisode(
    val id: Int,
    val name: String,
    val description: String,
    val image: List<String>,
    val links: List<RemotePodcastEpisodeExternalLink>,
    val duration: String,
    val releaseDate: String,
    val showId: Int? = null
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
