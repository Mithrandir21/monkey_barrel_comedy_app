package pm.bam.mbc.feature.podcasts.ui.episode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pm.bam.mbc.common.delayOnStart
import pm.bam.mbc.common.onError
import pm.bam.mbc.domain.models.Artist
import pm.bam.mbc.domain.models.PodcastEpisode
import pm.bam.mbc.domain.models.Show
import pm.bam.mbc.domain.repositories.artist.ArtistRepository
import pm.bam.mbc.domain.repositories.podcast.PodcastRepository
import pm.bam.mbc.domain.repositories.shows.ShowsRepository
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.fatal


@OptIn(ExperimentalCoroutinesApi::class)
internal class PodcastEpisodeViewModel(
    private val logger: Logger,
    private val podcastRepository: PodcastRepository,
    private val artistRepository: ArtistRepository,
    private val showsRepository: ShowsRepository
) : ViewModel() {


    // We store and react to the podcastEpisodeId changes so that only a single 'Podcast Episode' flow can exists
    private val podcastEpisodeIdFlow = MutableStateFlow<Long?>(null)

    private val _uiState = MutableStateFlow<PodcastEpisodeScreenData>(PodcastEpisodeScreenData.Loading)
    val uiState: StateFlow<PodcastEpisodeScreenData> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PodcastEpisodeScreenData.Loading
    )

    init {
        viewModelScope.launch {
            podcastEpisodeIdFlow
                .filterNotNull() // Skip our initial null value
                .distinctUntilChanged() // Skip fetching if podcastId is the same, like on orientation change
                .delayOnStart(550)
                .flatMapLatest { loadPodcastEpisode(it) }
                .collect { _uiState.emit(it) }
        }
    }

    fun reloadPodcastEpisodes(podcastId: Long) =
        viewModelScope.launch {
            loadPodcastEpisode(podcastId)
                .onStart { emit(PodcastEpisodeScreenData.Loading) }
                .collect { _uiState.emit(it) }
        }

    private fun loadPodcastEpisode(podcastId: Long): Flow<PodcastEpisodeScreenData> =
        flowOf(podcastId)
            .map { podcastRepository.getEpisode(it) }
            .flatMapLatest<PodcastEpisode, PodcastEpisodeScreenData> { episode ->
                val artist: Artist? = episode.artistId?.let { artistRepository.getArtist(it) }
                val show: Show? = episode.showId?.let { showsRepository.getShow(it) }

                flowOf(PodcastEpisodeScreenData.Success(podcastEpisode = episode, artist = artist, show = show))
            }
            .onError { fatal(logger, it) }
            .catch { emit(PodcastEpisodeScreenData.Error) }


    fun loadPodcastEpisodeDetails(podcastEpisodeId: Long) = podcastEpisodeIdFlow.update { podcastEpisodeId }


    internal sealed class PodcastEpisodeScreenData {
        data object Loading : PodcastEpisodeScreenData()
        data object Error : PodcastEpisodeScreenData()
        data class Success(
            val podcastEpisode: PodcastEpisode,
            val artist: Artist? = null,
            val show: Show? = null
        ) : PodcastEpisodeScreenData()
    }
}