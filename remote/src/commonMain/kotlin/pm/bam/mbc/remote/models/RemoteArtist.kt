package pm.bam.mbc.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class RemoteArtist(
    val id: Long,
    val firstname: String,
    val lastname: String? = null,
    val description: String,
    val images: List<String>,
    val genres: List<RemoteCategories>,
    val showsIds: List<Long>? = null,
    val merchIds: List<Long>? = null,
    val podcastsEpisodeIds: List<Long>? = null,
    val blogPostsIds: List<Long>? = null,
    val externalLinks: List<RemoteLink>? = null
)