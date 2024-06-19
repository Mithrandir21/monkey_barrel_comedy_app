package pm.bam.mbc.feature.shows.ui.show

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import pm.bam.mbc.common.toFlow
import pm.bam.mbc.domain.models.Artist
import pm.bam.mbc.domain.models.Merch
import pm.bam.mbc.domain.models.Show
import pm.bam.mbc.domain.repositories.artist.ArtistRepository
import pm.bam.mbc.domain.repositories.merch.MerchRepository
import pm.bam.mbc.domain.repositories.shows.ShowsRepository
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.fatal

@OptIn(ExperimentalCoroutinesApi::class)
internal class ShowViewModel(
    private val logger: Logger,
    private val showsRepository: ShowsRepository,
    private val artistRepository: ArtistRepository,
    private val merchRepository: MerchRepository
) : ViewModel() {

    // We store and react to the ShowId changes so that only a single 'show' flow can exists
    private val showIdFlow = MutableStateFlow<Long?>(null)

    private val _uiState = MutableStateFlow<ShowScreenData>(ShowScreenData.Loading)
    val uiState: StateFlow<ShowScreenData> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ShowScreenData.Loading
    )

    init {
        viewModelScope.launch {
            showIdFlow
                .filterNotNull() // Skip our initial null value
                .distinctUntilChanged() // Skip fetching if showId is the same, like on orientation change
                .delayOnStart(550)
                .flatMapLatest { loadShowFlow(it) }
                .collect { _uiState.emit(it) }
        }
    }


    fun reloadShow(showId: Long) {
        viewModelScope.launch {
            loadShowFlow(showId)
                .collect { _uiState.emit(it) }
        }
    }

    private fun loadShowFlow(showId: Long) =
        flowOf(showId)
            .flatMapLatest { showsRepository.getShow(it).toFlow() }
            .flatMapLatest<Show, ShowScreenData> { show ->
                val artists = show.artistIds?.let { artistRepository.getArtists(*it.toLongArray()) } ?: listOf()
                val merch = show.merchIds?.let { merchRepository.getMerch(*it.toLongArray()) } ?: listOf()

                ShowScreenData.Success(show, artists, merch).toFlow()
            }
            .onStart { _uiState.emit(ShowScreenData.Loading) }
            .onError { fatal(logger, it) }
            .catch { emit(ShowScreenData.Error) }


    fun loadShowDetails(showId: Long) = showIdFlow.update { showId }

    sealed class ShowScreenData {
        data object Loading : ShowScreenData()
        data object Error : ShowScreenData()
        data class Success(
            val show: Show,
            val artists: List<Artist> = listOf(),
            val merch: List<Merch> = listOf()
        ) : ShowScreenData()
    }
}