package pm.bam.mbc.feature.podcasts.ui.podcasts

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
import pm.bam.mbc.domain.models.Podcast
import pm.bam.mbc.domain.repositories.podcast.PodcastRepository
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.fatal

internal class PodcastsViewModel(
    private val logger: Logger,
    private val podcastRepository: PodcastRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PodcastsScreenData())
    val uiState: StateFlow<PodcastsScreenData> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PodcastsScreenData()
    )

    init {
        viewModelScope.launch {
            loadPodcastsScreenData()
                .collect { _uiState.emit(it) }
        }
    }

    fun loadData() =
        viewModelScope.launch {
            loadPodcastsScreenData()
                .onStart { emit(_uiState.value.copy(state = PodcastsScreenStatus.LOADING)) }
                .collect { _uiState.emit(it) }
        }

    private fun loadPodcastsScreenData() =
        flow { emitAll(podcastRepository.observePodcasts()) }
            .map { shows -> shows.sortedBy { it.id } }
            .map { PodcastsScreenData(state = PodcastsScreenStatus.SUCCESS, artists = it) }
            .onError { fatal(logger, it) }
            .catch { emit(PodcastsScreenData(state = PodcastsScreenStatus.ERROR)) }


    internal data class PodcastsScreenData(
        val state: PodcastsScreenStatus = PodcastsScreenStatus.LOADING,
        val artists: List<Podcast> = emptyList(),
    )

    internal enum class PodcastsScreenStatus {
        LOADING, ERROR, SUCCESS
    }
}