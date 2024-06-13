package pm.bam.mbc.remote.datasources

import pm.bam.mbc.remote.models.RemoteShow

interface RemoteShowsDataSource {

    suspend fun getAllShows(): List<RemoteShow>

}