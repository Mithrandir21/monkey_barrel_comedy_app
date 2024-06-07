package pm.bam.mbc.feature.artists.ui.artist

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import pm.bam.mbc.domain.models.Artist
import pm.bam.mbc.domain.repositories.artist.ArtistRepository
import pm.bam.mbc.feature.artists.ui.artists.ArtistsScreen
import pm.bam.mbc.feature.artists.ui.artists.ArtistsViewModel
import pm.bam.mbc.testing.TestingLoggingListener
import kotlin.test.Test


private val artistFlow = MutableStateFlow<List<Artist>>(emptyList())
private val baseArtist = Artist(1, "name", "desc", listOf("images"), listOf("genres"), listOf(1))


internal class ExampleTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun myTest() = runComposeUiTest {
        // Declares a mock UI to demonstrate API calls
        //
        // Replace with your own declarations to test the code of your project
        setContent {
            var text by remember { mutableStateOf("Hello") }
            Text(
                text = text,
                modifier = Modifier.testTag("text")
            )
            Button(
                onClick = { text = "Compose" },
                modifier = Modifier.testTag("button")
            ) {
                Text("Click me")
            }
        }

        // Tests the declared UI with assertions and actions of the Compose Multiplatform testing API
        onNodeWithTag("text").assertTextEquals("Hello")
        onNodeWithTag("button").performClick()
        onNodeWithTag("text").assertTextEquals("Compose")
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun myTest2() = runComposeUiTest {
        // Declares a mock UI to demonstrate API calls
        //
        // Replace with your own declarations to test the code of your project
        setContent {
            ArtistsScreen(
                onBack = { },
                onViewArtist = { },
                viewModel = ArtistsViewModel(TestingLoggingListener(), FakeArtistRepositoryForScreen())
            )
        }

        // Tests the declared UI with assertions and actions of the Compose Multiplatform testing API
        onNodeWithTag("text").assertTextEquals("Hello")
        onNodeWithTag("button").performClick()
        onNodeWithTag("text").assertTextEquals("Compose")
    }
}


private open class FakeArtistRepositoryForScreen : ArtistRepository {
    override fun observeArtists(): Flow<List<Artist>> = artistFlow
    override fun getArtist(artistId: Long): Artist = baseArtist
    override fun getArtists(vararg artistId: Long): List<Artist> = listOf(baseArtist)
    override fun refreshArtists(): Unit = Unit
}