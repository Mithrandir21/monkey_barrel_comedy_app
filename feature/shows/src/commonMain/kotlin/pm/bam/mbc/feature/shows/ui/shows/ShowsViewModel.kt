package pm.bam.mbc.feature.shows.ui.shows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pm.bam.mbc.common.flatMapLatestDelayAtLeast
import pm.bam.mbc.common.onError
import pm.bam.mbc.domain.models.Show
import pm.bam.mbc.domain.models.ShowSearchParameters
import pm.bam.mbc.domain.repositories.shows.ShowsRepository
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.fatal

internal const val SHOW_SEARCH_DELAY = 750L

internal class ShowsViewModel(
    private val logger: Logger,
    private val showsRepository: ShowsRepository
) : ViewModel() {

    // We store and react to the Query changes so that only a single search flow can exists
    private val searchParametersFlow = MutableStateFlow(ShowSearchParameters())

    private val _uiState = MutableStateFlow(ShowsScreenData())
    val uiState: StateFlow<ShowsScreenData> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ShowsScreenData()
    )

    init {
        viewModelScope.launch {
            searchParametersFlow
                .onEach { _uiState.emit(_uiState.value.copy(state = ShowsScreenStatus.LOADING)) }
                .flatMapLatestDelayAtLeast(SHOW_SEARCH_DELAY) { searchParameters -> showsRepository.searchShows(searchParameters) }
                .map { ShowsScreenData(state = ShowsScreenStatus.SUCCESS, shows = it) }
                .onError { fatal(logger, it) }
                .catch { emit(ShowsScreenData(state = ShowsScreenStatus.ERROR)) }
                .collect { _uiState.emit(it) }
        }
    }

    fun searchShows(searchParameters: ShowSearchParameters) {
        viewModelScope.launch {
            searchParametersFlow.emit(searchParameters)
        }
    }

    internal data class ShowsScreenData(
        val state: ShowsScreenStatus = ShowsScreenStatus.LOADING,
        val shows: List<Show> = emptyList(),
    )

    internal enum class ShowsScreenStatus {
        LOADING, ERROR, SUCCESS
    }
}