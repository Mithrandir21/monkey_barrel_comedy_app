package pm.bam.mbc.feature.artist.ui

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
import pm.bam.mbc.domain.models.Show
import pm.bam.mbc.domain.repositories.artist.ArtistRepository
import pm.bam.mbc.domain.repositories.shows.ShowsRepository
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.fatal

@OptIn(ExperimentalCoroutinesApi::class)
internal class ArtistViewModel(
    private val logger: Logger,
    private val artistRepository: ArtistRepository,
    private val showsRepository: ShowsRepository
) : ViewModel() {

    // We store and react to the artistId changes so that only a single 'artist' flow can exists
    private val artistIdFlow = MutableStateFlow<Long?>(null)

    private val _uiState = MutableStateFlow<ArtistScreenData>(ArtistScreenData.Loading)
    val uiState: StateFlow<ArtistScreenData> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ArtistScreenData.Loading
    )

    init {
        viewModelScope.launch {
            artistIdFlow
                .filterNotNull() // Skip our initial null value
                .distinctUntilChanged() // Skip fetching if artistId is the same, like on orientation change
                .delayOnStart(550)
                .flatMapLatest { loadArtistFlow(it) }
                .collect { _uiState.emit(it) }
        }
    }


    fun reloadArtists(artistId: Long) {
        viewModelScope.launch {
            loadArtistFlow(artistId)
                .collect { _uiState.emit(it) }
        }
    }

    private fun loadArtistFlow(artistId: Long) =
        flowOf(artistId)
            .flatMapLatest { artistRepository.getArtist(it).toFlow() }
            .flatMapLatest<Artist, ArtistScreenData> { artist ->
                artist.showsIds?.let { showsRepository.getShows(*it.toLongArray()) }
                    ?.toFlow()
                    ?.map { ArtistScreenData.Data(artist, it) }
                    ?: flowOf(ArtistScreenData.Data(artist))
            }
            .onStart { _uiState.emit(ArtistScreenData.Loading) }
            .onError { fatal(logger, it) }
            .catch { emit(ArtistScreenData.Error) }


    fun loadArtistDetails(artistId: Long) = artistIdFlow.update { artistId }

    internal sealed class ArtistScreenData {
        data object Loading : ArtistScreenData()
        data object Error : ArtistScreenData()
        data class Data(
            val artist: Artist,
            val shows: List<Show> = listOf()
        ) : ArtistScreenData()
    }
}