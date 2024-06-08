package pm.bam.mbc.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pm.bam.mbc.common.onError
import pm.bam.mbc.domain.models.News
import pm.bam.mbc.domain.models.Show
import pm.bam.mbc.domain.repositories.artist.ArtistRepository
import pm.bam.mbc.domain.repositories.blog.BlogRepository
import pm.bam.mbc.domain.repositories.news.NewsRepository
import pm.bam.mbc.domain.repositories.podcast.PodcastRepository
import pm.bam.mbc.domain.repositories.shows.ShowsRepository
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.fatal

internal const val NEWS_SHOWS = 5
internal const val LIMIT_SHOWS = 5

@OptIn(ExperimentalCoroutinesApi::class)
internal class HomeViewModel(
    private val logger: Logger,
    private val newsRepository: NewsRepository,
    private val showsRepository: ShowsRepository,
    private val artistRepository: ArtistRepository,
    private val podcastRepository: PodcastRepository,
    private val blogRepository: BlogRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeScreenData>(HomeScreenData.Loading)
    val uiState: StateFlow<HomeScreenData> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeScreenData.Loading
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
                .onStart { emit(HomeScreenData.Loading) }
                .collect { _uiState.emit(it) }
        }

    private fun loadHomeScreenData() =
        flow { emitAll(showsRepository.observeShows()) }
            .map { shows -> shows.sortedBy { it.startDate }.take(LIMIT_SHOWS) }
            .flatMapLatest<List<Show>, HomeScreenData> { shows ->
                newsRepository.observeNews()
                    .map { news -> HomeScreenData.Success(topUpcomingShows = shows, news = news.take(NEWS_SHOWS)) }

            }
            .onError { fatal(logger, it) }
            .catch { emit(HomeScreenData.Error) }

    private fun refreshAllData() =
        viewModelScope.launch {
            newsRepository.refreshNews()
            showsRepository.refreshShows()
            artistRepository.refreshArtists()
            podcastRepository.refreshPodcasts()
            podcastRepository.refreshEpisodes()
            blogRepository.refreshBlogPosts()
        }


    internal sealed class HomeScreenData {
        data object Loading : HomeScreenData()
        data object Error : HomeScreenData()
        data class Success(
            val topUpcomingShows: List<Show>,
            val news: List<News>,
        ) : HomeScreenData()
    }
}