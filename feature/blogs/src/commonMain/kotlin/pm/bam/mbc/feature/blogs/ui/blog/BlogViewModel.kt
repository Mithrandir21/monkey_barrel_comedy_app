package pm.bam.mbc.feature.blogs.ui.blog

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
import pm.bam.mbc.domain.models.BlogPost
import pm.bam.mbc.domain.repositories.blog.BlogRepository
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.fatal

internal class BlogViewModel(
    private val logger: Logger,
    private val blogRepository: BlogRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BlogScreenData())
    val uiState: StateFlow<BlogScreenData> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BlogScreenData()
    )

    init {
        viewModelScope.launch {
            loadBlogScreenData()
                .collect { _uiState.emit(it) }
        }
    }

    fun loadData() =
        viewModelScope.launch {
            loadBlogScreenData()
                .onStart { emit(_uiState.value.copy(state = BlogScreenStatus.LOADING)) }
                .collect { _uiState.emit(it) }
        }

    private fun loadBlogScreenData() =
        flow { emitAll(blogRepository.observeBlogPosts()) }
            .map { shows -> shows.sortedBy { it.id } }
            .map { BlogScreenData(state = BlogScreenStatus.SUCCESS, posts = it) }
            .onError { fatal(logger, it) }
            .catch { emit(BlogScreenData(state = BlogScreenStatus.ERROR)) }


    internal data class BlogScreenData(
        val state: BlogScreenStatus = BlogScreenStatus.LOADING,
        val posts: List<BlogPost> = emptyList(),
    )

    internal enum class BlogScreenStatus {
        LOADING, ERROR, SUCCESS
    }
}