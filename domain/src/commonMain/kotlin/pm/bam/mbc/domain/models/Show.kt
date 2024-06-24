package pm.bam.mbc.domain.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.properties.Properties
import pm.bam.mbc.domain.datetime.DateTimeParsing
import pm.bam.mbc.remote.models.RemoteEventStatus
import pm.bam.mbc.remote.models.RemoteShowSchedule
import pm.bam.mbc.remote.models.RemoteShowVenues

@Serializable
data class Show(
    val id: Long,
    val name: String,
    val description: String,
    val url: String,
    val images: List<String>,
    val categories: List<Categories>? = null,
    val artistIds: List<Long>? = null,
    val merchIds: List<Long>? = null,
    val schedule: List<ShowSchedule>,
)


@Serializable
enum class EventStatus {
    ACTIVE,
    CANCELLED,
}

internal fun RemoteEventStatus.toEventStatus(): EventStatus = when (this) {
    RemoteEventStatus.ACTIVE -> EventStatus.ACTIVE
    RemoteEventStatus.CANCELLED -> EventStatus.CANCELLED
}

enum class ShowVenues {
    MB1,
    MB2,
    MB3,
    MB4
}

internal fun RemoteShowVenues.toShowVenues(): ShowVenues = when (this) {
    RemoteShowVenues.MB1 -> ShowVenues.MB1
    RemoteShowVenues.MB2 -> ShowVenues.MB2
    RemoteShowVenues.MB3 -> ShowVenues.MB3
    RemoteShowVenues.MB4 -> ShowVenues.MB4
}

/* Keep in mind that these fields names must match the ones in the RemoteShowSchedule class. */
@Serializable
data class ShowSchedule(
    val id: Long,
    val status: EventStatus,
    val venue: ShowVenues,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val statusNote: String? = null
)

internal fun RemoteShowSchedule.toShowSchedule(dateTimeParsing: DateTimeParsing): ShowSchedule = ShowSchedule(
    id = this.id,
    status = this.status.toEventStatus(),
    venue = this.venue.toShowVenues(),
    start = dateTimeParsing.parseRemoteDatabaseDateTime(this.start),
    end = dateTimeParsing.parseRemoteDatabaseDateTime(this.end),
    statusNote = this.statusNote
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class ShowSearchParameters(
    val sortBy: ShowSearchSortByDirection = ShowSearchSortByDirection(),
    val title: String? = null,
    val titleExact: Boolean? = null,
    val priceRange: Pair<Int, Int>? = null,
    val dateTimeRange: Pair<Long?, Long?>? = null,
    val venues: List<ShowVenues> = listOf(),
    val categories: List<Categories> = listOf()
){

    /**
     * Encodes properties from the this [ShowSearchParameters] to a map.
     * `null` values are omitted from the output.
     *
     * @see ShowSearchParameters.from
     */
    fun asMap() = Properties.encodeToMap(serializer(), this)

    companion object {
        /**
         * Decodes properties from the given [map] to a value of type [ShowSearchParameters].
         * [ShowSearchParameters] may contain properties of nullable types; they will be filled by non-null values from the [map], if present.
         */
        fun from(map: Map<String, Any?>): ShowSearchParameters = Properties.decodeFromMap(serializer(),
            // Removes any map Key/Value pairs where the Value is NULL.
            map.mapNotNull { (key, value) -> value?.let { key to it } }
                .toMap())
    }

    /**
     * Returning `false` to avoid the default implementation of `equals` when attempting to emit a new value in a `StateFlow`.
     * See "Strong equality-based conflation" in the StateFlow documentation.
     */
    override fun equals(other: Any?): Boolean = false

    override fun hashCode(): Int = this::class.hashCode()
}

@Serializable
data class ShowSearchSortByDirection(
    val sortBy: ShowSearchSortBy = ShowSearchSortBy.DATE,
    val direction: SortDirection = SortDirection.BOTTOM_TO_TOP,
)

enum class SortDirection {
    TOP_TO_BOTTOM,
    BOTTOM_TO_TOP,
}

@Serializable
enum class ShowSearchSortBy {
    NAME,
    DATE,
    VENUE,
}