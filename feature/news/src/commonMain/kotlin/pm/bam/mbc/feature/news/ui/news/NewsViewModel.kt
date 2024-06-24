package pm.bam.mbc.feature.news.ui.news

import androidx.annotation.OpenForTesting
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
import pm.bam.mbc.domain.models.News
import pm.bam.mbc.domain.repositories.news.NewsRepository
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.fatal

@OpenForTesting
internal open class NewsViewModel(
    private val logger: Logger,
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewsScreenData())
    val uiState: StateFlow<NewsScreenData> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NewsScreenData()
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
                .onStart { emit(_uiState.value.copy(state = NewsScreenStatus.LOADING)) }
                .collect { _uiState.emit(it) }
        }

    private fun loadNewsScreenData() =
        flow { emitAll(newsRepository.observeNews()) }
            .map { shows -> shows.sortedBy { it.id } }
            .map { NewsScreenData(state = NewsScreenStatus.SUCCESS, news = it) }
            .onError { fatal(logger, it) }
            .catch { emit(NewsScreenData(state = NewsScreenStatus.ERROR)) }


    internal data class NewsScreenData(
        val state: NewsScreenStatus = NewsScreenStatus.LOADING,
        val news: List<News> = emptyList(),
    )

    internal enum class NewsScreenStatus {
        LOADING, ERROR, SUCCESS
    }
}