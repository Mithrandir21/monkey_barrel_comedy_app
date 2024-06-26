package pm.bam.mbc.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteShow(
    val id: Long,
    val title: String,
    val description: String,
    @SerialName("ticket_url")
    val url: String,
    val images: List<String>,
    val categories: List<RemoteCategories>? = null,
    @SerialName("merch_ids")
    val merchIds: List<IDsWrapper>? = null,

    // Not added initially, but constructed in the RemoteShowsDataSourceImpl
    val schedule: List<RemoteShowSchedule> = emptyList()
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
    @SerialName("show_id")
    val showId: Long,
    val status: RemoteEventStatus,
    val venue: RemoteShowVenues,
    val start: String,
    val end: String,
    @SerialName("artist_ids")
    val artistIds: List<IDsWrapper>? = null,
    @SerialName("status_note")
    val statusNote: String? = null
)