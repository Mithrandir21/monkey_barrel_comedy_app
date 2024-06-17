package pm.bam.mbc.domain.repositories.shows

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import pm.bam.mbc.common.serializer.Serializer
import pm.bam.mbc.domain.db.transformations.toDatabaseShow
import pm.bam.mbc.domain.db.transformations.toShow
import pm.bam.mbc.domain.models.Show
import pm.bam.mbc.domain.models.ShowSearchParameters
import pm.bam.mbc.domain.models.ShowSearchSortBy
import pm.bam.mbc.domain.models.SortDirection
import pm.bam.mbc.remote.datasources.RemoteShowsDataSource
import pmbammbcdomain.DatabaseShowQueries

internal class ShowsRepositoryImpl(
    private val serializer: Serializer,
    private val remoteShowDataSource: RemoteShowsDataSource,
    private val showQueries: DatabaseShowQueries
) : ShowsRepository {

    override fun observeShows(): Flow<List<Show>> =
        showQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { databaseShows -> databaseShows.map { it.toShow(serializer) } }

    override fun getShow(showId: Long): Show =
        showQueries.selectById(showId.toLong())
            .executeAsOne()
            .toShow(serializer)

    override fun getShows(vararg showId: Long): List<Show> =
        showQueries.selectByIds(showId.toList())
            .executeAsList()
            .map { it.toShow(serializer) }

    override fun searchShows(searchParameters: ShowSearchParameters): List<Show> {
        return showQueries.selectAll()
            .executeAsList()
            .map { it.toShow(serializer) }
            .filter { show -> searchParameters.title?.let { show.name.contains(it, ignoreCase = true) } ?: true }
            .let { shows ->
                searchParameters.venues.takeIf { it.isNotEmpty() }
                    ?.let { searchVenues -> shows.filter { show -> show.schedule.any { showSchedule -> searchVenues.contains(showSchedule.venue) } } }
                    ?: shows
            }
            .let { shows ->
                searchParameters.categories.takeIf { it.isNotEmpty() }
                    ?.let { searchCategories -> shows.filter { show -> show.category?.any { searchCategories.contains(it) } == true } }
                    ?: shows
            }
            .let { shows ->
                searchParameters.dateTimeRange?.let { (startLong, endLong) ->
                    val desiredRange = startLong..endLong

                    shows.filter { show ->
                        show.schedule.any { showSchedule ->
                            desiredRange.within(showSchedule.start.toUtcEpochMillis()..showSchedule.end.toUtcEpochMillis())
                        }
                    }
                } ?: shows
            }
            .let { shows ->
                val topToBottom = searchParameters.sortBy.direction == SortDirection.TOP_TO_BOTTOM

                when (searchParameters.sortBy.sortBy) {
                    ShowSearchSortBy.NAME -> when (topToBottom) {
                        true -> shows.sortedByDescending { show -> show.name }
                        false -> shows.sortedBy { show -> show.name }
                    }

                    ShowSearchSortBy.DATE -> when (topToBottom) {
                        true -> shows.sortedByDescending { show -> show.schedule.first().start }
                        false -> shows.sortedBy { show -> show.schedule.first().start }
                    }

                    ShowSearchSortBy.VENUE -> when (topToBottom) {
                        true -> shows.sortedByDescending { show -> show.schedule.first().venue }
                        false -> shows.sortedBy { show -> show.schedule.first().venue }
                    }
                }
            }
    }

    override suspend fun refreshShows() =
        remoteShowDataSource.getAllShows()
            .map { it.toDatabaseShow(serializer) }
            .toList()
            .let { shows ->
                showQueries.transaction {
                    showQueries.deleteAll()
                    shows.forEach { showQueries.insert(it) }
                }
            }
}

private fun LocalDateTime.toUtcEpochMillis() = toInstant(TimeZone.UTC).toEpochMilliseconds()

private fun ClosedRange<Long>.within(other: ClosedRange<Long>): Boolean =
    (this.start >= other.start && this.start <= other.endInclusive)
            && (this.endInclusive >= other.start && this.endInclusive <= other.endInclusive)