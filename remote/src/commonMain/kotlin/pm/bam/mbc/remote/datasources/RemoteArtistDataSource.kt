package pm.bam.mbc.remote.datasources

import pm.bam.mbc.remote.models.RemoteArtist

interface RemoteArtistDataSource {

    suspend fun getAllArtists(): List<RemoteArtist>

}