package pm.bam.mbc.remote.models

import kotlinx.serialization.Serializable


@Serializable
data class RemoteNews(
    val id: Long,
    val title: String,
    val description: String,
    val images: List<String>,
    val types: List<RemoteNewsType>,
    val showIds: List<Long>? = null,
    val merchIds: List<Long>? = null,
    val episodeIds: List<Long>? = null,
    val blogPostsIds: List<Long>? = null,
    val externalLinks: List<RemoteLink>? = null
)

enum class RemoteNewsType {
    COMEDY,
    STANDUP,
    BUSINESS,
    PODCAST,
    MERCH
}
