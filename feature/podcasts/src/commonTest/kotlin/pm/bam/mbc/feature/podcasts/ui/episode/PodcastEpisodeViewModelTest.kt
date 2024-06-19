package pm.bam.mbc.feature.podcasts.ui.episode

import app.cash.turbine.test
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDateTime
import pm.bam.mbc.domain.models.Artist
import pm.bam.mbc.domain.models.Categories
import pm.bam.mbc.domain.models.EventStatus
import pm.bam.mbc.domain.models.Podcast
import pm.bam.mbc.domain.models.PodcastEpisode
import pm.bam.mbc.domain.models.Show
import pm.bam.mbc.domain.models.ShowSchedule
import pm.bam.mbc.domain.models.ShowSearchParameters
import pm.bam.mbc.domain.models.ShowVenues
import pm.bam.mbc.domain.repositories.artist.ArtistRepository
import pm.bam.mbc.domain.repositories.podcast.PodcastRepository
import pm.bam.mbc.domain.repositories.shows.ShowsRepository
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.testing.TestingLoggingListener
import kotlin.test.BeforeTest
import kotlin.test.Test


private val podcastEpisodeFlow = MutableStateFlow<PodcastEpisode?>(null)
private val basePodcastEpisode = PodcastEpisode(1, "name", "desc", listOf(), listOf(), 123, "releaseDate", 1, listOf(1), listOf(1))
private val basePodcast = Podcast(1, "name", "desc", listOf(), listOf())

private val artistFlow = MutableStateFlow<List<Artist>>(emptyList())
private val baseArtist = Artist(1, "firstname", "lastname", "desc", listOf(), listOf())

private val showFlow = MutableStateFlow<List<Show>>(emptyList())
private val baseShow = Show(
    1, "name", "desc", "url", listOf("images"), listOf(Categories.COMEDY), listOf(1, 2, 3), schedule = listOf(
        ShowSchedule(1, EventStatus.ACTIVE, ShowVenues.MB1, LocalDateTime(2021, 1, 1, 1, 1), LocalDateTime(2021, 1, 1, 1, 1))
    )
)

internal class PodcastEpisodeViewModelTest {

    private val logger: Logger = TestingLoggingListener()

    private lateinit var viewModel: PodcastEpisodeViewModel


    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        viewModel = PodcastEpisodeViewModel(logger, FakePodcastRepository(), FakeArtistRepository(), FakeShowsRepository())
    }

    @Test
    fun `initially loading state`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe PodcastEpisodeViewModel.PodcastEpisodeScreenData.Loading
        }
    }

    @Test
    fun `load podcast episode`() = runTest {
        podcastEpisodeFlow.emit(basePodcastEpisode)
        artistFlow.emit(listOf(baseArtist))
        showFlow.emit(listOf(baseShow))

        viewModel.loadPodcastEpisodeDetails(1)

        viewModel.uiState.test {
            awaitItem() shouldBe PodcastEpisodeViewModel.PodcastEpisodeScreenData.Loading
            awaitItem() shouldBe PodcastEpisodeViewModel.PodcastEpisodeScreenData.Success(basePodcastEpisode, listOf(baseArtist), listOf(baseShow))
        }
    }

    @Test
    fun `reload podcast episode`() = runTest {
        podcastEpisodeFlow.emit(basePodcastEpisode)
        artistFlow.emit(listOf(baseArtist))
        showFlow.emit(listOf(baseShow))

        viewModel.reloadPodcastEpisodes(1)

        viewModel.uiState.test {
            awaitItem() shouldBe PodcastEpisodeViewModel.PodcastEpisodeScreenData.Loading
            awaitItem() shouldBe PodcastEpisodeViewModel.PodcastEpisodeScreenData.Success(basePodcastEpisode, listOf(baseArtist), listOf(baseShow))
        }
    }

    @Test
    fun `error state`() = runTest {
        viewModel = PodcastEpisodeViewModel(logger, object : FakePodcastRepository() {
            override fun getEpisode(episodeId: Long): PodcastEpisode = throw Exception()
        }, FakeArtistRepository(), FakeShowsRepository())

        viewModel.loadPodcastEpisodeDetails(1)

        viewModel.uiState.test {
            awaitItem() shouldBe PodcastEpisodeViewModel.PodcastEpisodeScreenData.Loading
            awaitItem() shouldBe PodcastEpisodeViewModel.PodcastEpisodeScreenData.Error
        }
    }
}

private open class FakePodcastRepository : PodcastRepository {
    override fun observePodcasts(): Flow<List<Podcast>> = flowOf(listOf(basePodcast))
    override fun getPodcasts(podcastId: Long): Podcast = basePodcast
    override fun observeEpisodes(podcastId: Long): Flow<List<PodcastEpisode>> = flowOf(listOf(basePodcastEpisode))
    override fun getEpisode(episodeId: Long): PodcastEpisode = basePodcastEpisode
    override suspend fun refreshPodcasts() = Unit
    override suspend fun refreshEpisodes() = Unit
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