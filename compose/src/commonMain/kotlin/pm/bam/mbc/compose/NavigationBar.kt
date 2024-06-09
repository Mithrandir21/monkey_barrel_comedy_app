package pm.bam.mbc.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiPeople
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicExternalOn
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import monkeybarrelcomey.compose.generated.resources.Res
import monkeybarrelcomey.compose.generated.resources.navigation_bar_artists_label
import monkeybarrelcomey.compose.generated.resources.navigation_bar_home_label
import monkeybarrelcomey.compose.generated.resources.navigation_bar_merch_label
import monkeybarrelcomey.compose.generated.resources.navigation_bar_podcast_label
import monkeybarrelcomey.compose.generated.resources.navigation_bar_shows_label
import org.jetbrains.compose.resources.stringResource

class NavigationBarConfig(
    val items: List<NavigationBarItemConfig>
)

class NavigationBarItemConfig(
    val imageVector: ImageVector,
    val contentDescription: String,
    val onSelected: () -> Unit,
    val label: String,
    val selected: Boolean = false
)

enum class BottomNavigationDestinations {
    HOME,
    SHOWS,
    ARTISTS,
    PODCASTS,
    MERCH
}

@Composable
fun createBottomNavigationConfig(
    selectedType: BottomNavigationDestinations,
    onHomeSelected: () -> Unit,
    onShowsSelected: () -> Unit,
    onArtistsSelected: () -> Unit,
    onPodcastsSelected: () -> Unit,
    onMerchSelected: () -> Unit
): NavigationBarConfig =
    NavigationBarConfig(
        listOf(
            NavigationBarItemConfig(
                imageVector = Icons.Default.EmojiPeople,
                contentDescription = stringResource(Res.string.navigation_bar_artists_label),
                onSelected = onArtistsSelected,
                label = stringResource(Res.string.navigation_bar_artists_label),
                selected = selectedType == BottomNavigationDestinations.ARTISTS
            ),
            NavigationBarItemConfig(
                imageVector = Icons.Filled.MicExternalOn,
                contentDescription = stringResource(Res.string.navigation_bar_shows_label),
                onSelected = onShowsSelected,
                label = stringResource(Res.string.navigation_bar_shows_label),
                selected = selectedType == BottomNavigationDestinations.SHOWS
            ),
            NavigationBarItemConfig(
                imageVector = Icons.Default.Home,
                contentDescription = stringResource(Res.string.navigation_bar_home_label),
                onSelected = onHomeSelected,
                label = stringResource(Res.string.navigation_bar_home_label),
                selected = selectedType == BottomNavigationDestinations.HOME
            ),
            NavigationBarItemConfig(
                imageVector = Icons.Default.Mic,
                contentDescription = stringResource(Res.string.navigation_bar_podcast_label),
                onSelected = onPodcastsSelected,
                label = stringResource(Res.string.navigation_bar_podcast_label),
                selected = selectedType == BottomNavigationDestinations.PODCASTS
            ),
            NavigationBarItemConfig(
                imageVector = Icons.Default.Sell,
                contentDescription = stringResource(Res.string.navigation_bar_merch_label),
                onSelected = onMerchSelected,
                label = stringResource(Res.string.navigation_bar_merch_label),
                selected = selectedType == BottomNavigationDestinations.MERCH
            )
        )
    )

@Composable
fun BottomNavigation(
    modifier: Modifier = Modifier,
    config: NavigationBarConfig
) {
    NavigationBar(
        modifier = modifier.fillMaxWidth(),
    ) {
        config.items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.imageVector,
                        contentDescription = item.contentDescription
                    )
                },
                onClick = item.onSelected,
                label = { Text(item.label) },
                selected = item.selected
            )
        }
    }
}