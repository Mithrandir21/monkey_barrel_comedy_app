package pm.bam.mbc.feature.podcasts.ui.episode

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import monkeybarrelcomey.common.generated.resources.image_placeholder
import monkeybarrelcomey.feature.podcasts.generated.resources.Res
import monkeybarrelcomey.feature.podcasts.generated.resources.podcasts_screen_artist_image_content_description
import monkeybarrelcomey.feature.podcasts.generated.resources.podcasts_screen_artists_label
import monkeybarrelcomey.feature.podcasts.generated.resources.podcasts_screen_data_loading_error_msg
import monkeybarrelcomey.feature.podcasts.generated.resources.podcasts_screen_data_loading_error_retry
import monkeybarrelcomey.feature.podcasts.generated.resources.podcasts_screen_loading_label
import monkeybarrelcomey.feature.podcasts.generated.resources.podcasts_screen_navigation_back_button
import monkeybarrelcomey.feature.podcasts.generated.resources.podcasts_screen_podcast_image_content_description
import monkeybarrelcomey.feature.podcasts.generated.resources.podcasts_screen_show_image_content_description
import monkeybarrelcomey.feature.podcasts.generated.resources.podcasts_screen_show_label
import monkeybarrelcomey.feature.podcasts.generated.resources.podcasts_screen_show_venue_label
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pm.bam.mbc.common.collectAsStateWithLifecycleFix
import pm.bam.mbc.common.theme.MonkeyCustomTheme
import pm.bam.mbc.common.theme.MonkeyTheme
import pm.bam.mbc.compose.ArtistRow
import pm.bam.mbc.compose.ShowRow
import pm.bam.mbc.domain.models.Artist
import pm.bam.mbc.domain.models.Show
import pm.bam.mbc.feature.podcasts.ui.episode.PodcastEpisodeViewModel.PodcastEpisodeScreenData


@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun PodcastEpisodeScreen(
    podcastEpisodeId: Long,
    onBack: () -> Unit,
    onViewShow: (showId: Long) -> Unit,
    onViewArtist: (artistId: Long) -> Unit,
    viewModel: PodcastEpisodeViewModel = koinViewModel<PodcastEpisodeViewModel>()
) {
    viewModel.loadPodcastEpisodeDetails(podcastEpisodeId)

    val data = viewModel.uiState.collectAsStateWithLifecycleFix()

    val onRetry: () -> Unit = { viewModel.reloadPodcastEpisodes(podcastEpisodeId) }

    ScreenScaffold(
        data = data.value,
        onBack = onBack,
        onViewShow = onViewShow,
        onViewArtist = onViewArtist,
        onRetry = onRetry
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenScaffold(
    data: PodcastEpisodeScreenData,
    onBack: () -> Unit,
    onViewShow: (showId: Long) -> Unit,
    onViewArtist: (artistId: Long) -> Unit,
    onRetry: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val title = when (data) {
        PodcastEpisodeScreenData.Loading, PodcastEpisodeScreenData.Error -> stringResource(Res.string.podcasts_screen_loading_label)
        is PodcastEpisodeScreenData.Success -> data.podcastEpisode.name
    }

    MonkeyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        modifier = Modifier.testTag(PodcastEpisodeScreenTopAppBarTag),
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        title = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) },
                        navigationIcon = {
                            IconButton(
                                modifier = Modifier.testTag(PodcastEpisodeScreenTopAppNavBarTag),
                                onClick = { onBack() }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(Res.string.podcasts_screen_navigation_back_button)
                                )
                            }
                        },
                        scrollBehavior = scrollBehavior,
                    )
                },
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
            ) { innerPadding: PaddingValues ->
                when (data) {
                    PodcastEpisodeScreenData.Loading -> CircularProgressIndicator(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                            .testTag(PodcastEpisodeScreenLoadingDataTag)
                    )

                    PodcastEpisodeScreenData.Error -> {
                        val message = stringResource(Res.string.podcasts_screen_data_loading_error_msg)
                        val actionLabel = stringResource(Res.string.podcasts_screen_data_loading_error_retry)

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

                    is PodcastEpisodeScreenData.Success -> PodcastEpisodeDetails(Modifier.padding(innerPadding), data, onViewShow, onViewArtist)
                }
            }
        }
    }
}


@Composable
private fun PodcastEpisodeDetails(
    modifier: Modifier,
    data: PodcastEpisodeScreenData.Success,
    onViewShow: (showId: Long) -> Unit,
    onViewArtist: (artistId: Long) -> Unit,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .testTag(PodcastEpisodeScreenTag),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = MonkeyCustomTheme.spacing.large, top = MonkeyCustomTheme.spacing.large, bottom = MonkeyCustomTheme.spacing.large)
                .testTag(PodcastEpisodeScreenDetailsTag)
        ) {
            AsyncImage(
                model = data.podcastEpisode.images.firstOrNull(),
                contentDescription = stringResource(Res.string.podcasts_screen_podcast_image_content_description, data.podcastEpisode.name),
                contentScale = ContentScale.Fit,
                error = painterResource(monkeybarrelcomey.common.generated.resources.Res.drawable.image_placeholder),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .height(200.dp)
                    .aspectRatio(1f)
            )

            Column(
                modifier = Modifier.padding(MonkeyCustomTheme.spacing.small)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MonkeyCustomTheme.spacing.small)
                        .testTag(PodcastEpisodeScreenDetailsTitleTag),
                    textAlign = TextAlign.Start,
                    text = data.podcastEpisode.name,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MonkeyCustomTheme.spacing.large),
            textAlign = TextAlign.Start,
            text = data.podcastEpisode.description
        )



        HorizontalDivider()

        data.show?.let {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MonkeyCustomTheme.spacing.large, vertical = MonkeyCustomTheme.spacing.small),
                textAlign = TextAlign.Start,
                text = stringResource(Res.string.podcasts_screen_show_label),
                style = MaterialTheme.typography.titleLarge,
            )

            ShowRow(
                modifier = Modifier.testTag(PodcastEpisodeScreenArtistRowTag.plus(it.id)),
                show = it,
                onShowSelected = onViewShow
            )
        }

        HorizontalDivider()

        data.artist?.let {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MonkeyCustomTheme.spacing.large, vertical = MonkeyCustomTheme.spacing.small),
                textAlign = TextAlign.Start,
                text = stringResource(Res.string.podcasts_screen_artists_label),
                style = MaterialTheme.typography.titleLarge,
            )

            ArtistRow(
                modifier = Modifier.testTag(PodcastEpisodeScreenArtistRowTag.plus(it.id)),
                artist = it,
                onViewArtist = onViewArtist
            )
        }

    }
}


internal const val PodcastEpisodeScreenTopAppBarTag = "PodcastEpisodeScreenTopAppBarTag"
internal const val PodcastEpisodeScreenTopAppNavBarTag = "PodcastEpisodeScreenTopAppNavBarTag"
internal const val PodcastEpisodeScreenLoadingDataTag = "PodcastEpisodeScreenLoadingDataTag"

internal const val PodcastEpisodeScreenDetailsTag = "PodcastEpisodeScreenDetailsTag"
internal const val PodcastEpisodeScreenDetailsTitleTag = "PodcastEpisodeScreenDetailsTitleTag"

internal const val PodcastEpisodeScreenTag = "PodcastEpisodeScreenTag"
internal const val PodcastEpisodeScreenArtistRowTag = "PodcastEpisodeScreenArtistRowTag"