package pm.bam.mbc.domain.repositories.artist

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pm.bam.mbc.common.serializer.Serializer
import pm.bam.mbc.domain.db.transformations.toArtist
import pm.bam.mbc.domain.db.transformations.toDatabaseArtist
import pm.bam.mbc.domain.models.Artist
import pm.bam.mbc.remote.datasources.RemoteArtistDataSource
import pmbammbcdomain.DatabaseArtistQueries

internal class ArtistRepositoryImpl(
    private val serializer: Serializer,
    private val remoteArtistDataSource: RemoteArtistDataSource,
    private val artistQueries: DatabaseArtistQueries
) : ArtistRepository {

    override fun observeArtists(): Flow<List<Artist>> =
        artistQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { databaseArtists -> databaseArtists.map { it.toArtist(serializer) } }

    override fun getArtist(artistId: Long): Artist =
        artistQueries.selectById(artistId)
            .executeAsOne()
            .toArtist(serializer)

    override fun getArtists(vararg artistId: Long): List<Artist> =
        artistQueries.selectByIds(artistId.toList())
            .executeAsList()
            .map { it.toArtist(serializer) }

    override suspend fun refreshArtists() =
        remoteArtistDataSource.getAllArtists()
            .map { it.toDatabaseArtist(serializer) }
            .toList()
            .let { artists ->
                artistQueries.transaction {
                    artistQueries.deleteAll()
                    artists.forEach { artistQueries.insert(it) }
                }
            }
}