package pm.bam.mbc.feature.blogs.ui.blog

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
import pm.bam.mbc.feature.blogs.ui.blog.BlogViewModel.BlogScreenData
import pm.bam.mbc.feature.blogs.ui.blog.BlogViewModel.BlogScreenStatus
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.testing.TestingLoggingListener
import kotlin.test.BeforeTest
import kotlin.test.Test

private val blogFlow = MutableStateFlow<List<BlogPost>>(emptyList())
private val baseBlogPost = BlogPost(1, "name", "desc", listOf("images"), listOf("tags"), "author", "date")

internal class BlogViewModelTest {

    private val logger: Logger = TestingLoggingListener()

    private lateinit var viewModel: BlogViewModel


    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        viewModel = BlogViewModel(logger, FakeBlogRepository())
    }

    @Test
    fun `initially loading state`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe BlogScreenData(BlogScreenStatus.LOADING, posts = emptyList())
            awaitItem() shouldBe BlogScreenData(BlogScreenStatus.SUCCESS, posts = emptyList())
        }
    }

    @Test
    fun `load data`() = runTest {
        blogFlow.emit(listOf(baseBlogPost))

        viewModel.loadData()

        viewModel.uiState.test {
            awaitItem() shouldBe BlogScreenData(BlogScreenStatus.LOADING, posts = emptyList())
            awaitItem() shouldBe BlogScreenData(BlogScreenStatus.SUCCESS, posts = listOf(baseBlogPost))
        }
    }

    @Test
    fun `error state`() = runTest {
        viewModel = BlogViewModel(logger, object : FakeBlogRepository() {
            override fun observeBlogPosts(): Flow<List<BlogPost>> = throw Exception()
        })

        viewModel.loadData()

        viewModel.uiState.test {
            awaitItem() shouldBe BlogScreenData(BlogScreenStatus.LOADING, posts = emptyList())
            awaitItem() shouldBe BlogScreenData(BlogScreenStatus.ERROR, posts = emptyList())
        }
    }
}

private open class FakeBlogRepository : BlogRepository {
    override fun observeBlogPosts(): Flow<List<BlogPost>> = blogFlow
    override fun getBlogPost(blogPostId: Long): BlogPost = baseBlogPost
    override fun refreshBlogPosts() = Unit
}