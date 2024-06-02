package pm.bam.mbc.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Podcast(
    val id: Long,
    val name: String,
    val description: String,
    val images: List<String>,
    val links: List<PodcastEpisodeExternalLink>
)

@Serializable
data class PodcastExternalLink(
    val url: String,
    val type: PodcastEpisodeExternalLinkType
)

enum class PodcastExternalLinkType {
    SPOTIFY,
    YOUTUBE,
    SOUNDCLOUD
}
