package pm.bam.mbc.feature.shows.ui.schedule

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

internal class ScheduleViewModelTest {

    private val logger: Logger = TestingLoggingListener()

    private lateinit var viewModel: ScheduleViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        viewModel = ScheduleViewModel(logger, FakeShowsRepository())
    }

    @Test
    fun `initially loading state`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe ScheduleViewModel.ScheduleScreenData.Loading
        }
    }

    @Test
    fun `load show schedule`() = runTest {
        showFlow.value = listOf(baseShow)

        viewModel.loadShowSchedule(1)

        viewModel.uiState.test {
            awaitItem() shouldBe ScheduleViewModel.ScheduleScreenData.Loading
            awaitItem() shouldBe ScheduleViewModel.ScheduleScreenData.Schedule(baseShow, baseShow.schedule)
        }
    }

    @Test
    fun `empty screen`() = runTest {
        viewModel = ScheduleViewModel(logger, object : FakeShowsRepository() {
            override fun getShow(showId: Long): Show = baseShow.copy(schedule = emptyList())
        })

        viewModel.loadShowSchedule(1)

        viewModel.uiState.test {
            awaitItem() shouldBe ScheduleViewModel.ScheduleScreenData.Loading
            awaitItem() shouldBe ScheduleViewModel.ScheduleScreenData.Empty
        }
    }

    @Test
    fun `error screen`() = runTest {
        viewModel = ScheduleViewModel(logger, object : FakeShowsRepository() {
            override fun getShow(showId: Long): Show = throw Exception()
        })

        viewModel.loadShowSchedule(1)

        viewModel.uiState.test {
            awaitItem() shouldBe ScheduleViewModel.ScheduleScreenData.Loading
            awaitItem() shouldBe ScheduleViewModel.ScheduleScreenData.Error
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