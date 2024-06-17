package pm.bam.mbc.feature.shows.ui.shows

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
import pm.bam.mbc.domain.models.Show
import pm.bam.mbc.domain.models.ShowSchedule
import pm.bam.mbc.domain.models.ShowSearchParameters
import pm.bam.mbc.domain.models.ShowVenues
import pm.bam.mbc.domain.repositories.shows.ShowsRepository
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.testing.TestingLoggingListener
import kotlin.test.BeforeTest
import kotlin.test.Test


private val showFlow = MutableStateFlow<List<Show>>(emptyList())
private val baseShow = Show(
    1, "name", "desc", "url", listOf("images"), listOf(Categories.COMEDY), listOf(1, 2, 3), schedule = listOf(
        ShowSchedule(1, EventStatus.ACTIVE, ShowVenues.MB1, LocalDateTime(2021, 1, 1, 1, 1), LocalDateTime(2021, 1, 1, 1, 1))
    )
)

internal class ShowsViewModelTest {

    private val logger: Logger = TestingLoggingListener()

    private lateinit var viewModel: ShowsViewModel


    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        viewModel = ShowsViewModel(logger, FakeShowsRepository())
    }

    @Test
    fun `initially loading state`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe ShowsViewModel.ShowsScreenData(ShowsViewModel.ShowsScreenStatus.LOADING)
            awaitItem() shouldBe ShowsViewModel.ShowsScreenData(ShowsViewModel.ShowsScreenStatus.SUCCESS, listOf(baseShow))
        }
    }

    @Test
    fun `load data`() = runTest {
        showFlow.emit(listOf(baseShow))

        viewModel.uiState.test {
            awaitItem() shouldBe ShowsViewModel.ShowsScreenData(ShowsViewModel.ShowsScreenStatus.LOADING)
            awaitItem() shouldBe ShowsViewModel.ShowsScreenData(ShowsViewModel.ShowsScreenStatus.SUCCESS, listOf(baseShow))
        }
    }

    @Test
    fun `error state`() = runTest {
        viewModel = ShowsViewModel(logger, object : FakeShowsRepository() {
            override fun searchShows(searchParameters: ShowSearchParameters): List<Show> = throw Exception()
        })

        viewModel.searchShows(ShowSearchParameters())

        viewModel.uiState.test {
            awaitItem() shouldBe ShowsViewModel.ShowsScreenData(ShowsViewModel.ShowsScreenStatus.LOADING)
            awaitItem() shouldBe ShowsViewModel.ShowsScreenData(ShowsViewModel.ShowsScreenStatus.ERROR)
        }
    }
}

private open class FakeShowsRepository : ShowsRepository {
    override fun observeShows(): Flow<List<Show>> = showFlow
    override fun getShow(showId: Long): Show = baseShow
    override fun getShows(vararg showId: Long): List<Show> = listOf(baseShow)
    override fun searchShows(searchParameters: ShowSearchParameters): List<Show> = listOf(baseShow)
    override suspend fun refreshShows() = Unit
}