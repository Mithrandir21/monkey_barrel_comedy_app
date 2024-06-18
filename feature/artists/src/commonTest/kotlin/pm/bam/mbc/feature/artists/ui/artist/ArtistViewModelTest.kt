package pm.bam.mbc.feature.artists.ui.artist

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
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.testing.TestingLoggingListener
import kotlin.test.BeforeTest
import kotlin.test.Test

private val artistFlow = MutableStateFlow<List<Artist>>(emptyList())
private val baseArtist = Artist(1, "firstname", "lastname", "desc", listOf("images"), listOf(Categories.COMEDY), listOf(1))

private val showFlow = MutableStateFlow<List<Show>>(emptyList())
private val baseShow = Show(
    1, "name", "desc", "url", listOf("images"), listOf(Categories.COMEDY), listOf(1, 2, 3), schedule = listOf(
        ShowSchedule(1, EventStatus.ACTIVE, ShowVenues.MB1, LocalDateTime(2021, 1, 1, 1, 1), LocalDateTime(2021, 1, 1, 1, 1))
    )
)

internal class ArtistViewModelTest {

    private val logger: Logger = TestingLoggingListener()

    private lateinit var viewModel: ArtistViewModel


    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        viewModel = ArtistViewModel(logger, FakeArtistRepository(), FakeShowsRepository())
    }

    @Test
    fun `initially loading state`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe ArtistViewModel.ArtistScreenData.Loading
        }
    }

    @Test
    fun `load artist`() = runTest {
        artistFlow.emit(listOf(baseArtist))
        showFlow.emit(listOf(baseShow))

        viewModel.loadArtistDetails(1)

        viewModel.uiState.test {
            awaitItem() shouldBe ArtistViewModel.ArtistScreenData.Loading
            awaitItem() shouldBe ArtistViewModel.ArtistScreenData.Success(baseArtist, listOf(baseShow))
        }
    }

    @Test
    fun `reload artist`() = runTest {
        artistFlow.emit(listOf(baseArtist))
        showFlow.emit(listOf(baseShow))

        viewModel.reloadArtist(1)

        viewModel.uiState.test {
            awaitItem() shouldBe ArtistViewModel.ArtistScreenData.Loading
            awaitItem() shouldBe ArtistViewModel.ArtistScreenData.Success(baseArtist, listOf(baseShow))
        }
    }

    @Test
    fun `error state`() = runTest {
        viewModel = ArtistViewModel(logger, object : FakeArtistRepository() {
            override fun getArtist(artistId: Long): Artist = throw Exception()
        }, FakeShowsRepository())

        viewModel.loadArtistDetails(1)

        viewModel.uiState.test {
            awaitItem() shouldBe ArtistViewModel.ArtistScreenData.Loading
            awaitItem() shouldBe ArtistViewModel.ArtistScreenData.Error
        }
    }
}

private open class FakeArtistRepository : ArtistRepository {
    override fun observeArtists(): Flow<List<Artist>> = artistFlow
    override fun getArtist(artistId: Long): Artist = baseArtist
    override fun getArtists(vararg artistId: Long): List<Artist> = listOf(baseArtist)
    override suspend fun refreshArtists(): Unit = Unit
}

private open class FakeShowsRepository : ShowsRepository {
    override fun observeShows(): Flow<List<Show>> = showFlow
    override fun getShow(showId: Long): Show = baseShow
    override fun getShows(vararg showId: Long): List<Show> = listOf(baseShow)
    override fun searchShows(searchParameters: ShowSearchParameters): List<Show> = listOf(baseShow)
    override suspend fun refreshShows() = Unit
}