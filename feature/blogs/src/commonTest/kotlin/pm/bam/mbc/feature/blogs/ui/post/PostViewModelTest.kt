package pm.bam.mbc.feature.blogs.ui.post

import app.cash.turbine.test
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import pm.bam.mbc.domain.models.BlogPost
import pm.bam.mbc.domain.repositories.blog.BlogRepository
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.testing.TestingLoggingListener
import kotlin.test.BeforeTest
import kotlin.test.Test

private val blogFlow = MutableStateFlow<List<BlogPost>>(emptyList())
private val baseBlogPost = BlogPost(1, "name", "desc", listOf("images"), listOf("tags"), "author", "date")

internal class PostViewModelTest {

    private val logger: Logger = TestingLoggingListener()

    private lateinit var viewModel: PostViewModel


    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        viewModel = PostViewModel(logger, FakeBlogRepository())
    }

    @Test
    fun `initially loading state`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe PostViewModel.BlogPostScreenData.Loading
        }
    }

    @Test
    fun `load blog posts`() = runTest {
        blogFlow.emit(listOf(baseBlogPost))

        viewModel.reloadBlogPosts(1)

        viewModel.uiState.test {
            awaitItem() shouldBe PostViewModel.BlogPostScreenData.Loading
            awaitItem() shouldBe PostViewModel.BlogPostScreenData.Success(baseBlogPost)
        }
    }

    @Test
    fun `reload blog posts`() = runTest {
        blogFlow.emit(listOf(baseBlogPost))

        viewModel.reloadBlogPosts(1)

        viewModel.uiState.test {
            awaitItem() shouldBe PostViewModel.BlogPostScreenData.Loading
            awaitItem() shouldBe PostViewModel.BlogPostScreenData.Success(baseBlogPost)
        }
    }

    @Test
    fun `error state`() = runTest {
        viewModel = PostViewModel(logger, object : FakeBlogRepository() {
            override fun getBlogPost(blogPostId: Long): BlogPost = throw Exception()
        })

        viewModel.reloadBlogPosts(1)

        viewModel.uiState.test {
            awaitItem() shouldBe PostViewModel.BlogPostScreenData.Loading
            awaitItem() shouldBe PostViewModel.BlogPostScreenData.Error
        }
    }
}

private open class FakeBlogRepository : BlogRepository {
    override fun observeBlogPosts(): Flow<List<BlogPost>> = blogFlow
    override fun getBlogPost(blogPostId: Long): BlogPost = baseBlogPost
    override fun refreshBlogPosts() = Unit
}