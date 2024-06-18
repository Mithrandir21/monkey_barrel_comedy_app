package pm.bam.mbc.feature.shows.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pm.bam.mbc.common.onError
import pm.bam.mbc.domain.models.Show
import pm.bam.mbc.domain.models.ShowSchedule
import pm.bam.mbc.domain.repositories.shows.ShowsRepository
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.fatal

internal class ScheduleViewModel(
    private val logger: Logger,
    private val showsRepository: ShowsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ScheduleScreenData>(ScheduleScreenData.Loading)
    val uiState: StateFlow<ScheduleScreenData> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ScheduleScreenData.Loading
    )

    fun loadShowSchedule(showId: Long) {
        viewModelScope.launch {
            flowOf(showId)
                .onStart { _uiState.emit(ScheduleScreenData.Loading) }
                .map { showsRepository.getShow(it) }
                .map { show -> ScheduleScreenData.Schedule(show, show.schedule) }
                .onError { fatal(logger, it) }
                .catch { _uiState.emit(ScheduleScreenData.Error) }
                .collect { _uiState.emit(it) }
        }
    }


    sealed class ScheduleScreenData {
        data object Loading : ScheduleScreenData()
        data object Empty : ScheduleScreenData()
        data object Error : ScheduleScreenData()
        data class Schedule(
            val show: Show,
            val schedule: List<ShowSchedule>
        ) : ScheduleScreenData()
    }
}