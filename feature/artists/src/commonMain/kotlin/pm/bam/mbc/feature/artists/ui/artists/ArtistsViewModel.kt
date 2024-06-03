package pm.bam.mbc.feature.artists.ui.artists

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
import pm.bam.mbc.domain.models.Artist
import pm.bam.mbc.domain.repositories.artist.ArtistRepository
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.fatal

internal class ArtistsViewModel(
    private val logger: Logger,
    private val artistRepository: ArtistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArtistsScreenData())
    val uiState: StateFlow<ArtistsScreenData> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ArtistsScreenData()
    )

    init {
        viewModelScope.launch {
            loadArtistsScreenData()
                .collect { _uiState.emit(it) }
        }
    }

    fun loadData() =
        viewModelScope.launch {
            loadArtistsScreenData()
                .onStart { emit(_uiState.value.copy(state = ArtistsScreenStatus.LOADING)) }
                .collect { _uiState.emit(it) }
        }

    private fun loadArtistsScreenData() =
        flow { emitAll(artistRepository.observeArtists()) }
            .map { shows -> shows.sortedBy { it.id } }
            .map { ArtistsScreenData(state = ArtistsScreenStatus.SUCCESS, artists = it) }
            .onError { fatal(logger, it) }
            .catch { emit(ArtistsScreenData(state = ArtistsScreenStatus.ERROR)) }


    internal data class ArtistsScreenData(
        val state: ArtistsScreenStatus = ArtistsScreenStatus.LOADING,
        val artists: List<Artist> = emptyList(),
    )

    internal enum class ArtistsScreenStatus {
        LOADING, ERROR, SUCCESS
    }
}