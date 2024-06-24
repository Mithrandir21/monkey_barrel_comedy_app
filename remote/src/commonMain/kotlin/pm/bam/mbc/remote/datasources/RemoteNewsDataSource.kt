package pm.bam.mbc.remote.datasources

import pm.bam.mbc.remote.models.RemoteNews

interface RemoteNewsDataSource {

    suspend fun getAllNews(): List<RemoteNews>

}