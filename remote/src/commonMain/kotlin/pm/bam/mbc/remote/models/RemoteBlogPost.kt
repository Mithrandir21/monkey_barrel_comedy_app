package pm.bam.mbc.remote.models

import kotlinx.serialization.Serializable


@Serializable
data class RemoteBlogPost(
    val id: Int,
    val title: String,
    val content: String,
    val image: List<String>,
    val tags: List<String>,
    val author: String,
    val releaseDate: String
)
