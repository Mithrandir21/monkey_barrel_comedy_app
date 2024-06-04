package pm.bam.mbc.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pm.bam.mbc.common.onError
import pm.bam.mbc.domain.models.Show
import pm.bam.mbc.domain.repositories.artist.ArtistRepository
import pm.bam.mbc.domain.repositories.blog.BlogRepository
import pm.bam.mbc.domain.repositories.podcast.PodcastRepository
import pm.bam.mbc.domain.repositories.shows.ShowsRepository
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.fatal

internal const val LIMIT_SHOWS = 5

internal class HomeViewModel(
    private val logger: Logger,
    private val showsRepository: ShowsRepository,
    private val artistRepository: ArtistRepository,
    private val podcastRepository: PodcastRepository,
    private val blogRepository: BlogRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenData())
    val uiState: StateFlow<HomeScreenData> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeScreenData()
    )

    init {
        refreshAllData()

        viewModelScope.launch {
            loadHomeScreenData()
                .collect { _uiState.emit(it) }
        }
    }

    fun loadData() =
        viewModelScope.launch {
            loadHomeScreenData()
                .onStart { emit(_uiState.value.copy(state = HomeScreenStatus.LOADING)) }
                .collect { _uiState.emit(it) }
        }

    private fun loadHomeScreenData() =
        flow { emitAll(showsRepository.observeShows()) }
            .map { shows -> shows.sortedBy { it.startDate }.take(LIMIT_SHOWS) }
            .map { HomeScreenData(state = HomeScreenStatus.SUCCESS, topUpcomingShows = it) }
            .onError { fatal(logger, it) }
            .catch { emit(HomeScreenData(state = HomeScreenStatus.ERROR)) }

    private fun refreshAllData() =
        viewModelScope.launch {
            showsRepository.refreshShows()
            artistRepository.refreshArtists()
            podcastRepository.refreshPodcasts()
            podcastRepository.refreshEpisodes()
            blogRepository.refreshBlogPosts()
        }

    internal data class HomeScreenData(
        val state: HomeScreenStatus = HomeScreenStatus.LOADING,
        val topUpcomingShows: List<Show> = emptyList(),
    )

    internal enum class HomeScreenStatus {
        LOADING, ERROR, SUCCESS
    }
}