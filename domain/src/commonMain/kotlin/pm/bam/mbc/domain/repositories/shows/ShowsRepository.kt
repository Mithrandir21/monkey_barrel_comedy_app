package pm.bam.mbc.domain.repositories.shows

import kotlinx.coroutines.flow.Flow
import pm.bam.mbc.domain.models.Show
import pm.bam.mbc.domain.models.ShowSearchParameters

interface ShowsRepository {

    fun observeShows(): Flow<List<Show>>

    fun getShow(showId: Long): Show

    fun getShows(vararg showId: Long): List<Show>

    fun searchShows(searchParameters: ShowSearchParameters): List<Show>

    suspend fun refreshShows()
}