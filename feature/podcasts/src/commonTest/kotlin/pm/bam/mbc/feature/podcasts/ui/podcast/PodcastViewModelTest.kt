package pm.bam.mbc.feature.podcasts.ui.podcast

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
import pm.bam.mbc.domain.models.Podcast
import pm.bam.mbc.domain.models.PodcastEpisode
import pm.bam.mbc.domain.repositories.podcast.PodcastRepository
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.testing.TestingLoggingListener
import kotlin.test.BeforeTest
import kotlin.test.Test


private val podcastEpisodesFlow = MutableStateFlow<List<PodcastEpisode>>(emptyList())
private val basePodcastEpisode = PodcastEpisode(1, "name", "desc", listOf(), listOf(), 1234, "releaseDate", 1, listOf(1), listOf(1))
private val basePodcast = Podcast(1, "name", "desc", listOf(), listOf())

internal class PodcastViewModelTest {

    private val logger: Logger = TestingLoggingListener()

    private lateinit var viewModel: PodcastViewModel


    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        viewModel = PodcastViewModel(logger, FakePodcastRepository())
    }

    @Test
    fun `initially loading state`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe PodcastViewModel.PodcastScreenData(PodcastViewModel.PodcastScreenStatus.LOADING)
        }
    }

    @Test
    fun `load podcast episodes`() = runTest {
        podcastEpisodesFlow.emit(listOf(basePodcastEpisode))

        viewModel.loadPodcastDetails(1)

        viewModel.uiState.test {
            awaitItem() shouldBe PodcastViewModel.PodcastScreenData(PodcastViewModel.PodcastScreenStatus.LOADING)
            awaitItem() shouldBe PodcastViewModel.PodcastScreenData(PodcastViewModel.PodcastScreenStatus.SUCCESS, listOf(basePodcastEpisode))
        }
    }

    @Test
    fun `reload podcast episodes`() = runTest {
        podcastEpisodesFlow.emit(listOf(basePodcastEpisode))

        viewModel.reloadPodcastEpisodes(1)

        viewModel.uiState.test {
            awaitItem() shouldBe PodcastViewModel.PodcastScreenData(PodcastViewModel.PodcastScreenStatus.LOADING)
            awaitItem() shouldBe PodcastViewModel.PodcastScreenData(PodcastViewModel.PodcastScreenStatus.SUCCESS, listOf(basePodcastEpisode))
        }
    }

    @Test
    fun `error state`() = runTest {
        viewModel = PodcastViewModel(logger, object : FakePodcastRepository() {
            override fun observeEpisodes(podcastId: Long): Flow<List<PodcastEpisode>> = throw Exception()
        })

        viewModel.loadPodcastDetails(1)

        viewModel.uiState.test {
            awaitItem() shouldBe PodcastViewModel.PodcastScreenData(PodcastViewModel.PodcastScreenStatus.LOADING)
            awaitItem() shouldBe PodcastViewModel.PodcastScreenData(PodcastViewModel.PodcastScreenStatus.ERROR)
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