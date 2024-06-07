package pm.bam.mbc.feature.artists.ui.artists

import app.cash.turbine.test
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import pm.bam.mbc.domain.models.Artist
import pm.bam.mbc.domain.repositories.artist.ArtistRepository
import pm.bam.mbc.feature.artists.ui.artists.ArtistsViewModel.ArtistsScreenData
import pm.bam.mbc.feature.artists.ui.artists.ArtistsViewModel.ArtistsScreenStatus
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.testing.TestingLoggingListener
import kotlin.test.BeforeTest
import kotlin.test.Test

private val artistFlow = MutableStateFlow<List<Artist>>(emptyList())
private val baseArtist = Artist(1, "name", "desc", listOf("images"), listOf("genres"))

internal class ArtistsViewModelTest {

    private val logger: Logger = TestingLoggingListener()

    private lateinit var viewModel: ArtistsViewModel


    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        viewModel = ArtistsViewModel(logger, FakeArtistRepository())
    }

    @Test
    fun `initially loading state`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe ArtistsScreenData(ArtistsScreenStatus.LOADING, artists = emptyList())
            awaitItem() shouldBe ArtistsScreenData(ArtistsScreenStatus.SUCCESS, artists = emptyList())
        }
    }

    @Test
    fun `load data`() = runTest {
        artistFlow.emit(listOf(baseArtist))

        viewModel.loadData()

        viewModel.uiState.test {
            awaitItem() shouldBe ArtistsScreenData(ArtistsScreenStatus.LOADING, artists = emptyList())
            awaitItem() shouldBe ArtistsScreenData(ArtistsScreenStatus.SUCCESS, artists = listOf(baseArtist))
        }
    }

    @Test
    fun `error state`() = runTest {
        viewModel = ArtistsViewModel(logger, object : FakeArtistRepository() {
            override fun observeArtists(): Flow<List<Artist>> = throw Exception()
        })

        viewModel.loadData()

        viewModel.uiState.test {
            awaitItem() shouldBe ArtistsScreenData(ArtistsScreenStatus.LOADING, artists = emptyList())
            awaitItem() shouldBe ArtistsScreenData(ArtistsScreenStatus.ERROR, artists = emptyList())
        }
    }
}

private open class FakeArtistRepository : ArtistRepository {
    override fun observeArtists(): Flow<List<Artist>> = artistFlow
    override fun getArtist(artistId: Long): Artist = baseArtist
    override fun getArtists(vararg artistId: Long): List<Artist> = emptyList()
    override fun refreshArtists(): Unit = Unit
}