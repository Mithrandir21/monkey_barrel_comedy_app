package pm.bam.mbc.feature.shows.ui.shows

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
import pm.bam.mbc.domain.repositories.shows.ShowsRepository
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.fatal

internal class ShowsViewModel(
    private val logger: Logger,
    private val showsRepository: ShowsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShowsScreenData())
    val uiState: StateFlow<ShowsScreenData> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ShowsScreenData()
    )

    init {
        viewModelScope.launch {
            loadShowsScreenData()
                .collect { _uiState.emit(it) }
        }
    }

    fun loadData() =
        viewModelScope.launch {
            loadShowsScreenData()
                .onStart { emit(_uiState.value.copy(state = ShowsScreenStatus.LOADING)) }
                .collect { _uiState.emit(it) }
        }

    private fun loadShowsScreenData() =
        flow { emitAll(showsRepository.observeShows()) }
            .map { shows -> shows.sortedBy { it.id } }
            .map { ShowsScreenData(state = ShowsScreenStatus.SUCCESS, shows = it) }
            .onError { fatal(logger, it) }
            .catch { emit(ShowsScreenData(state = ShowsScreenStatus.ERROR)) }


    internal data class ShowsScreenData(
        val state: ShowsScreenStatus = ShowsScreenStatus.LOADING,
        val shows: List<Show> = emptyList(),
    )

    internal enum class ShowsScreenStatus {
        LOADING, ERROR, SUCCESS
    }
}