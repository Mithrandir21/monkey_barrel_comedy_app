package pm.bam.mbc.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class News(
    val id: Long,
    val title: String,
    val description: String,
    val images: List<String>,
    val types: List<NewsType>,
    val showsIds: List<Long>? = null,
    val merchIds: List<Long>? = null,
    val podcastsIds: List<Long>? = null,
    val blogPostsIds: List<Long>? = null,
    val externalLinks: List<Link>? = null
)

enum class NewsType {
    COMEDY,
    STANDUP,
    BUSINESS,
    PODCAST,
    MERCH
}