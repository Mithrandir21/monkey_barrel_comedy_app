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
import pm.bam.mbc.domain.models.Merch
import pm.bam.mbc.domain.models.MerchItem
import pm.bam.mbc.domain.models.MerchItemStatus
import pm.bam.mbc.domain.models.MerchItemType
import pm.bam.mbc.domain.models.Show
import pm.bam.mbc.domain.models.ShowSchedule
import pm.bam.mbc.domain.models.ShowSearchParameters
import pm.bam.mbc.domain.models.ShowVenues
import pm.bam.mbc.domain.repositories.artist.ArtistRepository
import pm.bam.mbc.domain.repositories.merch.MerchRepository
import pm.bam.mbc.domain.repositories.shows.ShowsRepository
import pm.bam.mbc.feature.shows.ui.show.ShowViewModel.ShowScreenData
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.testing.TestingLoggingListener
import kotlin.test.BeforeTest
import kotlin.test.Test


private val showFlow = MutableStateFlow<List<Show>>(emptyList())
private val baseShow = Show(
    1, "name", "desc", "url", listOf("images"), listOf(Categories.COMEDY), merchIds = listOf(1), schedule = listOf(
        ShowSchedule(1, EventStatus.ACTIVE, ShowVenues.MB1, LocalDateTime(2021, 1, 1, 1, 1), LocalDateTime(2021, 1, 1, 1, 1), listOf(1, 2, 3))
    )
)
private val artistFlow = MutableStateFlow<List<Artist>>(emptyList())
private val baseArtist = Artist(1, "firstname", "lastname", "desc", listOf("images"), listOf(Categories.COMEDY))

private val merchFlow = MutableStateFlow<List<Merch>>(emptyList())
private val baseMerch = Merch(1, "name", "desc", listOf("images"))

private val merchItemFlow = MutableStateFlow<List<MerchItem>>(emptyList())
private val baseMerchItem = MerchItem(1, "name", "desc", MerchItemStatus.IN_STOCK, listOf(MerchItemType.VINYL), 1)


internal class ShowViewModelTest {

    private val logger: Logger = TestingLoggingListener()

    private lateinit var viewModel: ShowViewModel


    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        viewModel = ShowViewModel(logger, FakeShowsRepository(), FakeArtistRepository(), FakeMerchRepository())
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
        merchFlow.emit(listOf(baseMerch))

        viewModel.reloadShow(1)

        viewModel.uiState.test {
            awaitItem() shouldBe ShowScreenData.Loading
            awaitItem() shouldBe ShowScreenData.Success(baseShow, listOf(baseArtist), listOf(baseMerch))
        }
    }

    @Test
    fun `reload show`() = runTest {
        showFlow.emit(listOf(baseShow))
        artistFlow.emit(listOf(baseArtist))
        merchFlow.emit(listOf(baseMerch))

        viewModel.reloadShow(1)

        viewModel.uiState.test {
            awaitItem() shouldBe ShowScreenData.Loading
            awaitItem() shouldBe ShowScreenData.Success(baseShow, listOf(baseArtist), listOf(baseMerch))
        }
    }

    @Test
    fun `error state`() = runTest {
        viewModel = ShowViewModel(logger, object : FakeShowsRepository() {
            override fun getShow(showId: Long): Show = throw Exception()
        }, FakeArtistRepository(), FakeMerchRepository())

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

private open class FakeMerchRepository : MerchRepository {
    override fun observeMerch(): Flow<List<Merch>> = merchFlow
    override fun getMerch(merchId: Long): Merch = baseMerch
    override fun getMerch(vararg merchId: Long): List<Merch> = listOf(baseMerch)
    override fun observeMerchItems(merchId: Long): Flow<List<MerchItem>> = merchItemFlow
    override fun getMerchItem(merchItemId: Long): MerchItem = baseMerchItem
    override suspend fun refreshMerch(): Unit = Unit
    override suspend fun refreshMerchItems(): Unit = Unit
}