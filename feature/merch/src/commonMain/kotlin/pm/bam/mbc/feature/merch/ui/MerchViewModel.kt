package pm.bam.mbc.feature.merch.ui

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
import pm.bam.mbc.domain.models.Merch
import pm.bam.mbc.domain.repositories.merch.MerchRepository
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.fatal

internal class MerchViewModel(
    private val logger: Logger,
    private val merchRepository: MerchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MerchScreenData())
    val uiState: StateFlow<MerchScreenData> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MerchScreenData()
    )

    init {
        viewModelScope.launch {
            loadNewsScreenData()
                .collect { _uiState.emit(it) }
        }
    }

    fun loadData() =
        viewModelScope.launch {
            loadNewsScreenData()
                .onStart { emit(_uiState.value.copy(state = MerchScreenStatus.LOADING)) }
                .collect { _uiState.emit(it) }
        }

    private fun loadNewsScreenData() =
        flow { emitAll(merchRepository.observeMerch()) }
            .map { shows -> shows.sortedBy { it.id } }
            .map { MerchScreenData(state = MerchScreenStatus.SUCCESS, news = it) }
            .onError { fatal(logger, it) }
            .catch { emit(MerchScreenData(state = MerchScreenStatus.ERROR)) }


    internal data class MerchScreenData(
        val state: MerchScreenStatus = MerchScreenStatus.LOADING,
        val news: List<Merch> = emptyList(),
    )

    internal enum class MerchScreenStatus {
        LOADING, ERROR, SUCCESS
    }
}