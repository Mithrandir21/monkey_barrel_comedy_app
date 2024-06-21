package pm.bam.mbc.feature.podcasts.ui.podcasts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import coil3.compose.AsyncImage
import monkeybarrelcomey.common.generated.resources.data_loading_error_msg
import monkeybarrelcomey.common.generated.resources.data_loading_error_retry
import monkeybarrelcomey.common.generated.resources.image_placeholder
import monkeybarrelcomey.common.generated.resources.podcasts_image_content_description
import monkeybarrelcomey.feature.podcasts.generated.resources.Res
import monkeybarrelcomey.feature.podcasts.generated.resources.podcasts_screen_podcasts_header_label
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pm.bam.mbc.common.collectAsStateWithLifecycleFix
import pm.bam.mbc.compose.BottomNavigation
import pm.bam.mbc.compose.NavigationBarConfig
import pm.bam.mbc.compose.theme.MonkeyCustomTheme
import pm.bam.mbc.compose.theme.MonkeyTheme
import pm.bam.mbc.domain.models.Podcast
import pm.bam.mbc.feature.podcasts.ui.podcasts.PodcastsViewModel.PodcastsScreenData
import pm.bam.mbc.feature.podcasts.ui.podcasts.PodcastsViewModel.PodcastsScreenStatus.ERROR
import pm.bam.mbc.feature.podcasts.ui.podcasts.PodcastsViewModel.PodcastsScreenStatus.LOADING
import monkeybarrelcomey.common.generated.resources.Res as CommonRes

@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun PodcastsScreen(
    bottomNavConfig: NavigationBarConfig,
    onViewPodcast: (podcastId: Long, title: String) -> Unit,
    viewModel: PodcastsViewModel = koinViewModel<PodcastsViewModel>()
) {
    val data = viewModel.uiState.collectAsStateWithLifecycleFix()

    val onRetry: () -> Unit = { viewModel.loadData() }

    Screen(
        data = data.value,
        bottomNavConfig = bottomNavConfig,
        onViewPodcast = onViewPodcast,
        onRetry = onRetry
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Screen(
    data: PodcastsScreenData,
    bottomNavConfig: NavigationBarConfig,
    onViewPodcast: (podcastId: Long, title: String) -> Unit,
    onRetry: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    MonkeyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            modifier = Modifier.testTag(PodcastsScreenTopAppBarTag),
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text(
                                    text = stringResource(Res.string.podcasts_screen_podcasts_header_label),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            scrollBehavior = scrollBehavior,
                        )
                    },
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    bottomBar = { BottomNavigation(config = bottomNavConfig) }
                ) { innerPadding: PaddingValues ->
                    LazyVerticalGrid(
                        modifier = Modifier.padding(innerPadding),
                        columns = GridCells.Fixed(1),
                        content = {
                            items(data.podcasts.size) { index ->
                                PodcastCard(data.podcasts[index], onViewPodcast)
                            }
                        }
                    )

                    if (data.state == LOADING) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center)
                                .testTag(PodcastsScreenLoadingDataTag)
                        )
                    }
                }
            }

            if (data.state == ERROR) {
                val message = stringResource(CommonRes.string.data_loading_error_msg)
                val actionLabel = stringResource(CommonRes.string.data_loading_error_retry)

                LaunchedEffect(snackbarHostState) {
                    val results = snackbarHostState.showSnackbar(
                        message = message,
                        actionLabel = actionLabel
                    )
                    if (results == SnackbarResult.ActionPerformed) {
                        onRetry()
                    }
                }
            }
        }
    }
}


@Composable
private fun PodcastCard(
    podcast: Podcast,
    onViewPodcast: (podcastId: Long, title: String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MonkeyCustomTheme.spacing.medium)
            .clip(RoundedCornerShape(MonkeyCustomTheme.spacing.medium))
            .clickable(onClick = { onViewPodcast(podcast.id, podcast.name) })
    ) {
        Column {
            Box(modifier = Modifier.aspectRatio(1f).fillMaxWidth()) {
                AsyncImage(
                    model = podcast.images.firstOrNull(),
                    contentDescription = stringResource(CommonRes.string.podcasts_image_content_description, podcast.name),
                    contentScale = ContentScale.Fit,
                    error = painterResource(CommonRes.drawable.image_placeholder),
                )
            }

            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(MonkeyCustomTheme.spacing.medium),
                text = podcast.name,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MonkeyCustomTheme.spacing.medium),
                textAlign = TextAlign.Start,
                text = podcast.description,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

internal const val PodcastsScreenTopAppBarTag = "PodcastsScreenTopAppBarTag"
internal const val PodcastsScreenTopAppNavBarTag = "PodcastsScreenTopAppNavBarTag"
internal const val PodcastsScreenLoadingDataTag = "PodcastsScreenLoadingDataTag"