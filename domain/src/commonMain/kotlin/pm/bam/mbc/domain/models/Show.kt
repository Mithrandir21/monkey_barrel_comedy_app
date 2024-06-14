package pm.bam.mbc.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Show(
    val id: Long,
    val name: String,
    val description: String,
    val url: String,
    val images: List<String>,
    val category: List<Categories>? = null,
    val artistIds: List<Long>? = null,
    val schedule: List<ShowSchedule>,
)


@Serializable
enum class EventStatus {
    ACTIVE,
    CANCELLED,
}

enum class ShowVenues {
    MB1,
    MB2,
    MB3,
    MB4
}

/* Keep in mind that these fields names must match the ones in the RemoteShowSchedule class. */
@Serializable
data class ShowSchedule(
    val id: Long,
    val status: EventStatus,
    val venue: ShowVenues,
    val start: String,
    val end: String
)

@Serializable
data class ShowSearchParameters(
    val sortBy: ShowSearchSortByDirection = ShowSearchSortByDirection(),
    val title: String? = null,
    val lowerPrice: Int? = null,
    val upperPrice: Int? = null,
    val startDateTime: Long? = null,
    val endDateTime: Long? = null,
    val venue: ShowVenues? = null,
    val categories: List<Categories>? = null,
)

@Serializable
data class ShowSearchSortByDirection(
    val sortBy: ShowSearchSortBy = ShowSearchSortBy.DATE,
    val direction: SortDirection = SortDirection.ASCENDING,
)

enum class SortDirection {
    ASCENDING,
    DESCENDING,
}

@Serializable
enum class ShowSearchSortBy {
    NAME,
    DATE,
    VENUE,
}