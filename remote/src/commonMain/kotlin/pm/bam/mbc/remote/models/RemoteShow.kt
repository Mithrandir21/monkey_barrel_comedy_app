package pm.bam.mbc.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class RemoteShow(
    val id: Long,
    val name: String,
    val url: String,
    val venue: String,
    val images: List<String>,
    val eventStatus: RemoteEventStatus,
    val description: String,
    val category: List<String>? = null,
    val artistIds: List<Long>? = null,
    val startDate: String,
    val endDate: String,
)


enum class RemoteEventStatus {
    ACTIVE,
    CANCELLED,
}