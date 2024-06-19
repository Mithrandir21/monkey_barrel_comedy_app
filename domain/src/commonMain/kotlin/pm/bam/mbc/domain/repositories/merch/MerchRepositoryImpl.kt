package pm.bam.mbc.domain.repositories.merch

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pm.bam.mbc.common.serializer.Serializer
import pm.bam.mbc.domain.db.transformations.toDatabaseMerch
import pm.bam.mbc.domain.db.transformations.toDatabaseMerchItem
import pm.bam.mbc.domain.db.transformations.toMerch
import pm.bam.mbc.domain.db.transformations.toMerchItem
import pm.bam.mbc.domain.models.Merch
import pm.bam.mbc.domain.models.MerchItem
import pm.bam.mbc.remote.datasources.RemoteMerchDataSource
import pmbammbcdomain.DatabaseMerchItemQueries
import pmbammbcdomain.DatabaseMerchQueries

internal class MerchRepositoryImpl(
    private val serializer: Serializer,
    private val remoteMerchDataSource: RemoteMerchDataSource,
    private val merchQueries: DatabaseMerchQueries,
    private val merchItemQueries: DatabaseMerchItemQueries
) : MerchRepository {

    override fun observeMerch(): Flow<List<Merch>> =
        merchQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { databaseMerch -> databaseMerch.map { it.toMerch(serializer) } }

    override fun getMerch(merchId: Long): Merch =
        merchQueries.selectById(merchId)
            .executeAsOne()
            .toMerch(serializer)

    override fun observeMerchItems(merchId: Long): Flow<List<MerchItem>> =
        merchItemQueries.selectByMerchId(merchId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { databaseMerchItems -> databaseMerchItems.map { it.toMerchItem(serializer) } }

    override fun getMerchItem(merchItemId: Long): MerchItem =
        merchItemQueries.selectById(merchItemId)
            .executeAsOne()
            .toMerchItem(serializer)

    override suspend fun refreshMerch() =
        remoteMerchDataSource.getAllMerch()
            .map { it.toDatabaseMerch(serializer) }
            .toList()
            .let { merch ->
                merchQueries.transaction {
                    merchQueries.deleteAll()
                    merch.forEach { merchQueries.insert(it) }
                }
            }

    override suspend fun refreshMerchItems() =
        remoteMerchDataSource.getAllMerchItem()
            .map { it.toDatabaseMerchItem(serializer) }
            .toList()
            .let { merchItems ->
                merchItemQueries.transaction {
                    merchItemQueries.deleteAll()
                    merchItems.forEach { merchItemQueries.insert(it) }
                }
            }
}