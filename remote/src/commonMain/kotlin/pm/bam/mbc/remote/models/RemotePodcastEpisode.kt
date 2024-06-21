package pm.bam.mbc.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemotePodcastEpisode(
    val id: Long,
    val name: String,
    val description: String,
    val images: List<String>,
    val duration: Long,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("podcast_id")
    val podcastId: Long,
    @SerialName("show_ids")
    val showId: List<IDsWrapper>? = null,
    @SerialName("artist_ids")
    val artistId: List<IDsWrapper>? = null,
    val links: List<RemoteLink>,
)