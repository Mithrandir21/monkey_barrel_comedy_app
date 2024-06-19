package pm.bam.mbc.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Podcast(
    val id: Long,
    val name: String,
    val description: String,
    val images: List<String>,
    val links: List<Link>
)

@Serializable
data class PodcastEpisode(
    val id: Long,
    val name: String,
    val description: String,
    val images: List<String>,
    val links: List<Link>,
    val duration: Long,
    val releaseDate: String,
    val podcastId: Long,
    val showId: List<Long>? = null,
    val artistId: List<Long>? = null
)