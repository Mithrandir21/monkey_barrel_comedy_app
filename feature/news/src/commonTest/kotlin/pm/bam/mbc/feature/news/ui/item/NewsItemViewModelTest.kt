package pm.bam.mbc.feature.news.ui.item

import app.cash.turbine.test
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDateTime
import pm.bam.mbc.domain.models.Categories
import pm.bam.mbc.domain.models.EventStatus
import pm.bam.mbc.domain.models.News
import pm.bam.mbc.domain.models.Show
import pm.bam.mbc.domain.models.ShowSchedule
import pm.bam.mbc.domain.models.ShowSearchParameters
import pm.bam.mbc.domain.models.ShowVenues
import pm.bam.mbc.domain.repositories.news.NewsRepository
import pm.bam.mbc.domain.repositories.shows.ShowsRepository
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.testing.TestingLoggingListener
import kotlin.test.BeforeTest
import kotlin.test.Test

private val newsFlow = MutableStateFlow<List<News>>(emptyList())
private val baseNews = News(1, "title", "desc", listOf(), listOf(), listOf(1))

private val showFlow = MutableStateFlow<List<Show>>(emptyList())
private val baseShow = Show(
    1, "name", "desc", "url", listOf("images"), listOf(Categories.COMEDY), listOf(1, 2, 3), schedule = listOf(
        ShowSchedule(1, EventStatus.ACTIVE, ShowVenues.MB1, LocalDateTime(2021, 1, 1, 1, 1), LocalDateTime(2021, 1, 1, 1, 1))
    )
)


@OptIn(ExperimentalCoroutinesApi::class)
internal class NewsItemViewModelTest {

    private val logger: Logger = TestingLoggingListener()

    private lateinit var viewModel: NewsItemViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        viewModel = NewsItemViewModel(logger, FakeNewsRepository(), FakeShowsRepository())
    }

    @Test
    fun `initially loading state`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe NewsItemViewModel.NewsItemScreenData.Loading
        }
    }

    @Test
    fun `load data`() = runTest {
        newsFlow.emit(listOf(baseNews))

        viewModel.loadNewsDetails(1)

        viewModel.uiState.test {
            awaitItem() shouldBe NewsItemViewModel.NewsItemScreenData.Loading
            awaitItem() shouldBe NewsItemViewModel.NewsItemScreenData.Success(newsItem = baseNews, shows = listOf(baseShow))
        }
    }

    @Test
    fun `error state`() = runTest {
        viewModel = NewsItemViewModel(logger, object : FakeNewsRepository() {
            override fun getNews(newsId: Long): News = throw Exception()
        }, FakeShowsRepository())

        viewModel.loadNewsDetails(1)

        viewModel.uiState.test {
            awaitItem() shouldBe NewsItemViewModel.NewsItemScreenData.Loading
            awaitItem() shouldBe NewsItemViewModel.NewsItemScreenData.Error
        }
    }

}

private open class FakeNewsRepository : NewsRepository {
    override fun observeNews(): Flow<List<News>> = newsFlow
    override fun getNews(newsId: Long): News = baseNews
    override fun getNews(vararg newsId: Long): List<News> = listOf(baseNews)
    override suspend fun refreshNews() = Unit
}

private open class FakeShowsRepository : ShowsRepository {
    override fun observeShows(): Flow<List<Show>> = showFlow
    override fun getShow(showId: Long): Show = baseShow
    override fun getShows(vararg showId: Long): List<Show> = listOf(baseShow)
    override fun searchShows(searchParameters: ShowSearchParameters): List<Show> = listOf(baseShow)
    override suspend fun refreshShows() = Unit
}