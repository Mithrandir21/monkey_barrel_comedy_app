package pm.bam.mbc.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class RemotePodcastEpisode(
    val id: Long,
    val name: String,
    val description: String,
    val images: List<String>,
    val links: List<RemoteLink>,
    val duration: Long,
    val releaseDate: String,
    val podcastId: Long,
    val showId: List<Long>? = null,
    val artistId: List<Long>? = null
)