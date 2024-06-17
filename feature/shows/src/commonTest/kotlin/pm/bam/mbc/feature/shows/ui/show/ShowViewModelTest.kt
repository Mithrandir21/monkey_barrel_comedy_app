package pm.bam.mbc.feature.shows.ui.show

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
import pm.bam.mbc.domain.models.Artist
import pm.bam.mbc.domain.models.Categories
import pm.bam.mbc.domain.models.EventStatus
import pm.bam.mbc.domain.models.Show
import pm.bam.mbc.domain.models.ShowSchedule
import pm.bam.mbc.domain.models.ShowSearchParameters
import pm.bam.mbc.domain.models.ShowVenues
import pm.bam.mbc.domain.repositories.artist.ArtistRepository
import pm.bam.mbc.domain.repositories.shows.ShowsRepository
import pm.bam.mbc.feature.shows.ui.show.ShowViewModel.ShowScreenData
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
private val artistFlow = MutableStateFlow<List<Artist>>(emptyList())
private val baseArtist = Artist(1, "name", "desc", listOf("images"), listOf(Categories.COMEDY))


internal class ShowViewModelTest {

    private val logger: Logger = TestingLoggingListener()

    private lateinit var viewModel: ShowViewModel


    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        viewModel = ShowViewModel(logger, FakeShowsRepository(), FakeArtistRepository())
    }

    @Test
    fun `initially loading state`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe ShowScreenData.Loading
        }
    }

    @Test
    fun `load show`() = runTest {
        showFlow.emit(listOf(baseShow))
        artistFlow.emit(listOf(baseArtist))

        viewModel.reloadShow(1)

        viewModel.uiState.test {
            awaitItem() shouldBe ShowScreenData.Loading
            awaitItem() shouldBe ShowScreenData.Success(baseShow, listOf(baseArtist))
        }
    }

    @Test
    fun `reload show`() = runTest {
        showFlow.emit(listOf(baseShow))
        artistFlow.emit(listOf(baseArtist))

        viewModel.reloadShow(1)

        viewModel.uiState.test {
            awaitItem() shouldBe ShowScreenData.Loading
            awaitItem() shouldBe ShowScreenData.Success(baseShow, listOf(baseArtist))
        }
    }

    @Test
    fun `error state`() = runTest {
        viewModel = ShowViewModel(logger, object : FakeShowsRepository() {
            override fun getShow(showId: Long): Show = throw Exception()
        }, FakeArtistRepository())

        viewModel.reloadShow(1)

        viewModel.uiState.test {
            awaitItem() shouldBe ShowScreenData.Loading
            awaitItem() shouldBe ShowScreenData.Error
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

private open class FakeArtistRepository : ArtistRepository {
    override fun observeArtists(): Flow<List<Artist>> = artistFlow
    override fun getArtist(artistId: Long): Artist = baseArtist
    override fun getArtists(vararg artistId: Long): List<Artist> = listOf(baseArtist)
    override suspend fun refreshArtists(): Unit = Unit
}