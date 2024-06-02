package pm.bam.mbc.domain.repositories.artist

import kotlinx.coroutines.flow.Flow
import pm.bam.mbc.domain.models.Artist

interface ArtistRepository {

    fun observeArtists(): Flow<List<Artist>>

    fun getArtist(artistId: Long): Artist

    fun getArtists(vararg artistId: Long): List<Artist>

    fun refreshArtists()


}