package pm.bam.mbc.feature.news.ui.news

import app.cash.turbine.test
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import pm.bam.mbc.domain.models.News
import pm.bam.mbc.domain.repositories.news.NewsRepository
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.testing.TestingLoggingListener
import kotlin.test.BeforeTest
import kotlin.test.Test

private val newsFlow = MutableStateFlow<List<News>>(emptyList())
private val baseNews = News(1, "title", "desc", listOf(), listOf(), listOf(1))


@OptIn(ExperimentalCoroutinesApi::class)
internal class NewsViewModelTest {

    private val logger: Logger = TestingLoggingListener()

    private lateinit var viewModel: NewsViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        viewModel = NewsViewModel(logger, FakeNewsRepository())
    }

    @Test
    fun `initially loading state`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe NewsViewModel.NewsScreenData(NewsViewModel.NewsScreenStatus.LOADING, news = emptyList())
        }
    }

    @Test
    fun `load data`() = runTest {
        newsFlow.emit(listOf(baseNews))

        viewModel.loadData()

        viewModel.uiState.test {
            awaitItem() shouldBe NewsViewModel.NewsScreenData(NewsViewModel.NewsScreenStatus.LOADING, news = emptyList())
            awaitItem() shouldBe NewsViewModel.NewsScreenData(NewsViewModel.NewsScreenStatus.SUCCESS, news = listOf(baseNews))
        }
    }

    @Test
    fun `error state`() = runTest {
        viewModel = NewsViewModel(logger, object : FakeNewsRepository() {
            override fun observeNews(): Flow<List<News>> = throw Exception()
        })

        viewModel.loadData()

        viewModel.uiState.test {
            awaitItem() shouldBe NewsViewModel.NewsScreenData(NewsViewModel.NewsScreenStatus.LOADING, news = emptyList())
            awaitItem() shouldBe NewsViewModel.NewsScreenData(NewsViewModel.NewsScreenStatus.ERROR, news = emptyList())
        }
    }
}

private open class FakeNewsRepository : NewsRepository {
    override fun observeNews(): Flow<List<News>> = newsFlow
    override fun getNews(newsId: Long): News = baseNews
    override fun getNews(vararg newsId: Long): List<News> = listOf(baseNews)
    override suspend fun refreshNews() = Unit
}