package pm.bam.mbc.domain.repositories.shows

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pm.bam.mbc.common.serializer.Serializer
import pm.bam.mbc.domain.db.transformations.toDatabaseShow
import pm.bam.mbc.domain.db.transformations.toShow
import pm.bam.mbc.domain.models.Show
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

    override fun refreshShows() =
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