package pm.bam.mbc.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteArtist(
    val id: Long,
    val firstname: String,
    val lastname: String? = null,
    val description: String,
    val images: List<String>,
    val genres: List<RemoteCategories>,
    @SerialName("show_ids")
    val showsIds: List<IDsWrapper>? = null,
    @SerialName("merch_ids")
    val merchIds: List<IDsWrapper>? = null,
    @SerialName("episode_ids")
    val podcastsEpisodeIds: List<IDsWrapper>? = null,
    val blogPostsIds: List<IDsWrapper>? = null,
    @SerialName("links")
    val externalLinks: List<RemoteLink>? = null
)