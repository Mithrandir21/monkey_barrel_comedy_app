package pm.bam.mbc.remote.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class RemoteShow(
    val id: Long,
    val title: String,
    val url: String,
    val images: List<String>,
    val description: String,
    val categories: List<RemoteCategories>? = null,
    val artistIds: List<Long>? = null,
    val schedule: List<RemoteShowSchedule>
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

@Serializable
data class RemoteShowSchedule(
    val id: Long,
    val status: RemoteEventStatus,
    val venue: RemoteShowVenues,
    val start: LocalDateTime,
    val end: LocalDateTime
)