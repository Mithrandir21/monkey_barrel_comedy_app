package pm.bam.mbc.domain.repositories.shows

import kotlinx.coroutines.flow.Flow
import pm.bam.mbc.domain.models.Show

interface ShowsRepository {

    fun observeShows(): Flow<List<Show>>

    fun getShow(showId: Long): Show

    fun getShows(vararg showId: Long): List<Show>

    suspend fun refreshShows()
}