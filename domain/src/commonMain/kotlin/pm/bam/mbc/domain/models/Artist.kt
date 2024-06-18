package pm.bam.mbc.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Artist(
    val id: Long,
    val firstname: String,
    val lastname: String?,
    val description: String,
    val images: List<String>,
    val genres: List<Categories>,
    val showsIds: List<Long>? = null,
    val podcastsIds: List<Long>? = null,
    val blogPostsIds: List<Long>? = null,
    val externalLinks: List<Link>? = null
) {
    fun getFullName(): String = firstname
        .let { lastname?.let { lastname -> "$it $lastname" } }
        ?: firstname
}