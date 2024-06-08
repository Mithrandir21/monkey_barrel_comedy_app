package pm.bam.mbc.feature.news.ui.item

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
import pm.bam.mbc.domain.models.News
import pm.bam.mbc.domain.models.Show
import pm.bam.mbc.domain.repositories.news.NewsRepository
import pm.bam.mbc.domain.repositories.shows.ShowsRepository
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.fatal

@OptIn(ExperimentalCoroutinesApi::class)
internal class NewsItemViewModel(
    private val logger: Logger,
    private val newsRepository: NewsRepository,
    private val showsRepository: ShowsRepository
) : ViewModel() {

    // We store and react to the newsId changes so that only a single 'news' flow can exists
    private val newsIdFlow = MutableStateFlow<Long?>(null)

    private val _uiState = MutableStateFlow<NewsItemScreenData>(NewsItemScreenData.Loading)
    val uiState: StateFlow<NewsItemScreenData> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NewsItemScreenData.Loading
    )

    init {
        viewModelScope.launch {
            newsIdFlow
                .filterNotNull() // Skip our initial null value
                .distinctUntilChanged() // Skip fetching if newsId is the same, like on orientation change
                .delayOnStart(550)
                .flatMapLatest { loadNewsFlow(it) }
                .collect { _uiState.emit(it) }
        }
    }


    fun reloadNews(newsId: Long) {
        viewModelScope.launch {
            loadNewsFlow(newsId)
                .collect { _uiState.emit(it) }
        }
    }

    private fun loadNewsFlow(newsId: Long) =
        flowOf(newsId)
            .flatMapLatest { newsRepository.getNews(it).toFlow() }
            .flatMapLatest<News, NewsItemScreenData> { news ->
                news.showsIds?.let { showsRepository.getShows(*it.toLongArray()) }
                    ?.toFlow()
                    ?.map { NewsItemScreenData.Success(news, it) }
                    ?: flowOf(NewsItemScreenData.Success(news))
            }
            .onStart { _uiState.emit(NewsItemScreenData.Loading) }
            .onError { fatal(logger, it) }
            .catch { emit(NewsItemScreenData.Error) }


    fun loadNewsDetails(newsId: Long) = newsIdFlow.update { newsId }

    internal sealed class NewsItemScreenData {
        data object Loading : NewsItemScreenData()
        data object Error : NewsItemScreenData()
        data class Success(
            val newsItem: News,
            val shows: List<Show> = listOf()
        ) : NewsItemScreenData()
    }
}