package pm.bam.mbc.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class RemoteShow(
    val id: Long,
    val title: String,
    val url: String,
    val venues: List<RemoteShowVenues>,
    val images: List<String>,
    val eventStatus: RemoteEventStatus,
    val description: String,
    val categories: List<RemoteCategories>? = null,
    val artistIds: List<Long>? = null,
    val startDate: String,
    val endDate: String,
)


enum class RemoteEventStatus {
    ACTIVE,
    CANCELLED,
}

enum class RemoteShowVenues {
    MB1,
    MB2,
    MB3,
    MB4
}