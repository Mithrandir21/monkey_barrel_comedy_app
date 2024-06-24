package pm.bam.mbc.remote.models

import kotlinx.serialization.Serializable


@Serializable
data class RemoteBlogPost(
    val id: Long,
    val title: String,
    val content: String,
    val images: List<String>,
    val tags: List<String>,
    val author: String,
    val releaseDate: String
)
