package pm.bam.mbc.feature.blogs.ui.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
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
import pm.bam.mbc.domain.models.BlogPost
import pm.bam.mbc.domain.repositories.blog.BlogRepository
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.fatal

@OptIn(ExperimentalCoroutinesApi::class)
internal class PostViewModel(
    private val logger: Logger,
    private val blogRepository: BlogRepository
) : ViewModel() {

    // We store and react to the blogPostId changes so that only a single 'blog post' flow can exists
    private val blogPostIdFlow = MutableStateFlow<Long?>(null)

    private val _uiState = MutableStateFlow<BlogPostScreenData>(BlogPostScreenData.Loading)
    val uiState: StateFlow<BlogPostScreenData> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BlogPostScreenData.Loading
    )

    init {
        viewModelScope.launch {
            blogPostIdFlow
                .filterNotNull() // Skip our initial null value
                .distinctUntilChanged() // Skip fetching if blogPostId is the same, like on orientation change
                .delayOnStart(550)
                .flatMapLatest { loadBlogPost(it) }
                .collect { _uiState.emit(it) }
        }
    }

    fun reloadBlogPosts(blogPostId: Long) =
        viewModelScope.launch {
            loadBlogPost(blogPostId)
                .onStart { emit(BlogPostScreenData.Loading) }
                .collect { _uiState.emit(it) }
        }

    private fun loadBlogPost(blogPostId: Long): Flow<BlogPostScreenData> =
        flowOf(blogPostId)
            .map { blogRepository.getBlogPost(it) }
            .map<BlogPost, BlogPostScreenData> { BlogPostScreenData.Success(blogPost = it) }
            .onError { fatal(logger, it) }
            .catch { emit(BlogPostScreenData.Error) }


    fun loadBlogPostDetails(blogPostId: Long) = blogPostIdFlow.update { blogPostId }


    internal sealed class BlogPostScreenData {
        data object Loading : BlogPostScreenData()
        data object Error : BlogPostScreenData()
        data class Success(val blogPost: BlogPost) : BlogPostScreenData()
    }
}