package pm.bam.mbc.domain.repositories.merch

import kotlinx.coroutines.flow.Flow
import pm.bam.mbc.domain.models.Merch
import pm.bam.mbc.domain.models.MerchItem

interface MerchRepository {

    fun observeMerch(): Flow<List<Merch>>

    fun getMerch(merchId: Long): Merch

    fun observeMerchItems(merchId: Long): Flow<List<MerchItem>>

    fun getMerchItem(merchItemId: Long): MerchItem

    suspend fun refreshMerch()

    suspend fun refreshMerchItems()

}