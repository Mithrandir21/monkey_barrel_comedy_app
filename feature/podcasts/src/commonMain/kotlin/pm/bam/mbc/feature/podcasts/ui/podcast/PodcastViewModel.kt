package pm.bam.mbc.feature.podcasts.ui.podcast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pm.bam.mbc.common.delayOnStart
import pm.bam.mbc.common.onError
import pm.bam.mbc.domain.models.PodcastEpisode
import pm.bam.mbc.domain.repositories.podcast.PodcastRepository
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.fatal


@OptIn(ExperimentalCoroutinesApi::class)
internal class PodcastViewModel(
    private val logger: Logger,
    private val podcastRepository: PodcastRepository
) : ViewModel() {

    // We store and react to the podcastId changes so that only a single 'Podcast' flow can exists
    private val podcastIdFlow = MutableStateFlow<Long?>(null)

    private val _uiState = MutableStateFlow(PodcastScreenData())
    val uiState: StateFlow<PodcastScreenData> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PodcastScreenData()
    )

    init {
        viewModelScope.launch {
            podcastIdFlow
                .filterNotNull() // Skip our initial null value
                .distinctUntilChanged() // Skip fetching if podcastId is the same, like on orientation change
                .delayOnStart(550)
                .flatMapLatest { loadPodcastEpisodes(it) }
                .collect { _uiState.emit(it) }
        }
    }

    fun reloadPodcastEpisodes(podcastId: Long) =
        viewModelScope.launch {
            loadPodcastEpisodes(podcastId)
                .onStart { emit(_uiState.value.copy(state = PodcastScreenStatus.LOADING)) }
                .collect { _uiState.emit(it) }
        }

    private fun loadPodcastEpisodes(podcastId: Long) =
        flow { emitAll(podcastRepository.observeEpisodes(podcastId)) }
            .map { shows -> shows.sortedBy { it.id } }
            .map { PodcastScreenData(state = PodcastScreenStatus.SUCCESS, episodes = it) }
            .onError { fatal(logger, it) }
            .catch { emit(PodcastScreenData(state = PodcastScreenStatus.ERROR)) }


    fun loadPodcastDetails(podcastId: Long) = podcastIdFlow.update { podcastId }

    internal data class PodcastScreenData(
        val state: PodcastScreenStatus = PodcastScreenStatus.LOADING,
        val episodes: List<PodcastEpisode> = emptyList(),
    )

    internal enum class PodcastScreenStatus {
        LOADING, ERROR, SUCCESS
    }
}