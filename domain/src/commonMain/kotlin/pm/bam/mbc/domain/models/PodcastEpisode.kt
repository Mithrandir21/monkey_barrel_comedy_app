package pm.bam.mbc.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class PodcastEpisode(
    val id: Long,
    val name: String,
    val description: String,
    val images: List<String>,
    val links: List<PodcastEpisodeExternalLink>,
    val duration: String,
    val releaseDate: String,
    val podcastId: Long,
    val showId: Long? = null,
    val artistId: Long? = null
)

@Serializable
data class PodcastEpisodeExternalLink(
    val url: String,
    val type: PodcastEpisodeExternalLinkType
)

enum class PodcastEpisodeExternalLinkType {
    SPOTIFY,
    YOUTUBE,
    SOUNDCLOUD
}
