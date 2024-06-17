package pm.bam.mbc.feature.home.ui

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
import pm.bam.mbc.domain.models.BlogPost
import pm.bam.mbc.domain.models.Categories
import pm.bam.mbc.domain.models.EventStatus
import pm.bam.mbc.domain.models.News
import pm.bam.mbc.domain.models.Podcast
import pm.bam.mbc.domain.models.PodcastEpisode
import pm.bam.mbc.domain.models.Show
import pm.bam.mbc.domain.models.ShowSchedule
import pm.bam.mbc.domain.models.ShowSearchParameters
import pm.bam.mbc.domain.models.ShowVenues
import pm.bam.mbc.domain.repositories.artist.ArtistRepository
import pm.bam.mbc.domain.repositories.blog.BlogRepository
import pm.bam.mbc.domain.repositories.news.NewsRepository
import pm.bam.mbc.domain.repositories.podcast.PodcastRepository
import pm.bam.mbc.domain.repositories.shows.ShowsRepository
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.testing.TestingLoggingListener
import kotlin.test.BeforeTest
import kotlin.test.Test


private val podcastEpisodeFlow = MutableStateFlow<PodcastEpisode?>(null)
private val basePodcastEpisode = PodcastEpisode(1, "name", "desc", listOf(), listOf(), 1234, "releaseDate", 1, listOf(1), listOf(1))
private val basePodcast = Podcast(1, "name", "desc", listOf(), listOf())

private val artistFlow = MutableStateFlow<List<Artist>>(emptyList())
private val baseArtist = Artist(1, "name", "desc", listOf(), listOf())

private val newsFlow = MutableStateFlow<List<News>>(emptyList())
private val baseNews = News(1, "title", "desc", listOf(), listOf())

private val showFlow = MutableStateFlow<List<Show>>(emptyList())
private val baseShow = Show(1, "name", "desc", "url", listOf("images"), listOf(Categories.COMEDY), listOf(1, 2, 3), schedule = listOf(
        ShowSchedule(1, EventStatus.ACTIVE, ShowVenues.MB1, LocalDateTime(2021, 1, 1, 1, 1), LocalDateTime(2021, 1, 1, 1, 1))
    )
)

private val blogFlow = MutableStateFlow<List<BlogPost>>(emptyList())
private val baseBlogPost = BlogPost(1, "name", "desc", listOf("images"), listOf("tags"), "author", "date")


internal class HomeViewModelTest {

    private val logger: Logger = TestingLoggingListener()

    private lateinit var viewModel: HomeViewModel


    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        viewModel = HomeViewModel(logger, FakeNewsRepository(), FakeShowsRepository(), FakeArtistRepository(), FakePodcastRepository(), FakeBlogRepository())
    }

    @Test
    fun `initially loading state`() = runTest {
        viewModel.uiState.test {
            awaitItem() shouldBe HomeViewModel.HomeScreenData.Loading
        }
    }

    @Test
    fun `load data`() = runTest {
        podcastEpisodeFlow.emit(basePodcastEpisode)
        artistFlow.emit(listOf(baseArtist))
        showFlow.emit(listOf(baseShow))
        newsFlow.emit(listOf(baseNews))

        viewModel.loadData()

        viewModel.uiState.test {
            awaitItem() shouldBe HomeViewModel.HomeScreenData.Loading
            awaitItem() shouldBe HomeViewModel.HomeScreenData.Success(listOf(baseShow), listOf(baseArtist), listOf(baseNews))
        }
    }

    @Test
    fun `error state`() = runTest {
        viewModel = HomeViewModel(logger, FakeNewsRepository(), object : FakeShowsRepository() {
            override fun observeShows(): Flow<List<Show>> = throw Exception()
        }, FakeArtistRepository(), FakePodcastRepository(), FakeBlogRepository())

        viewModel.loadData()

        viewModel.uiState.test {
            awaitItem() shouldBe HomeViewModel.HomeScreenData.Loading
            awaitItem() shouldBe HomeViewModel.HomeScreenData.Error
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

private open class FakeNewsRepository : NewsRepository {
    override fun observeNews(): Flow<List<News>> = newsFlow
    override fun getNews(newsId: Long): News = baseNews
    override fun getNews(vararg newsId: Long): List<News> = listOf(baseNews)
    override suspend fun refreshNews() = Unit
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

private open class FakeBlogRepository : BlogRepository {
    override fun observeBlogPosts(): Flow<List<BlogPost>> = blogFlow
    override fun getBlogPost(blogPostId: Long): BlogPost = baseBlogPost
    override fun refreshBlogPosts() = Unit
}