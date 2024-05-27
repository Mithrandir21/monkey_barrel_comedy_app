package pm.bam.mbc.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class RemoteShow(
    val id: Int,
    val name: String,
    val url: String,
    val venue: String,
    val image: List<String>,
    val eventStatus: EventStatus,
    val description: String,
    val category: List<String>? = null,
    val artistIds: List<Int>? = null,
    val startDate: String,
    val endDate: String,
)


enum class EventStatus {
    ACTIVE,
    CANCELLED,
}