package pm.bam.mbc.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RemoteNews(
    val id: Long,
    val title: String,
    val description: String,
    val images: List<String>,
    val types: List<RemoteNewsType>,
    @SerialName("show_ids")
    val showIds: List<IDsWrapper>? = null,
    @SerialName("merch_ids")
    val merchIds: List<IDsWrapper>? = null,
    @SerialName("episode_ids")
    val episodeIds: List<IDsWrapper>? = null,
    val blogPostsIds: List<IDsWrapper>? = null,
    val externalLinks: List<RemoteLink>? = null
)

enum class RemoteNewsType {
    COMEDY,
    STANDUP,
    BUSINESS,
    PODCAST,
    MERCH
}
