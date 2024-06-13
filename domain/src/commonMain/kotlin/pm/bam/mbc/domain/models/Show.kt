package pm.bam.mbc.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Show(
    val id: Long,
    val name: String,
    val description: String,
    val url: String,
    val venue: String,
    val images: List<String>,
    val eventStatus: EventStatus,
    val category: List<Categories>? = null,
    val artistIds: List<Long>? = null,
    val startDate: String,
    val endDate: String,
)


enum class EventStatus {
    ACTIVE,
    CANCELLED,
}