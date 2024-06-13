package pm.bam.mbc.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Artist(
    val id: Long,
    val name: String,
    val description: String,
    val images: List<String>,
    val genres: List<Categories>,
    val showsIds: List<Long>? = null,
    val podcastsIds: List<Long>? = null,
    val blogPostsIds: List<Long>? = null,
    val externalLinks: List<Link>? = null
)