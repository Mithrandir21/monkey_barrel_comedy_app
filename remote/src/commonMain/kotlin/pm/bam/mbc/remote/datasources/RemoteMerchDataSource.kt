package pm.bam.mbc.remote.datasources

import pm.bam.mbc.remote.models.RemoteMerch
import pm.bam.mbc.remote.models.RemoteMerchItem

interface RemoteMerchDataSource {

    suspend fun getAllMerch(): List<RemoteMerch>

    suspend fun getAllMerchItem(): List<RemoteMerchItem>

}