package pm.bam.mbc.feature.artists.ui.artist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import monkeybarrelcomey.feature.artists.generated.resources.Res
import monkeybarrelcomey.feature.artists.generated.resources.artists_screen_artists_image_content_description
import monkeybarrelcomey.feature.artists.generated.resources.artists_screen_data_loading_error_msg
import monkeybarrelcomey.feature.artists.generated.resources.artists_screen_data_loading_error_retry
import monkeybarrelcomey.feature.artists.generated.resources.artists_screen_loading_label
import monkeybarrelcomey.feature.artists.generated.resources.artists_screen_navigation_back_button
import monkeybarrelcomey.feature.artists.generated.resources.artists_screen_show_venue_label
import monkeybarrelcomey.feature.artists.generated.resources.artists_screen_shows_label
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pm.bam.mbc.common.collectAsStateWithLifecycleFix
import pm.bam.mbc.common.theme.MonkeyCustomTheme
import pm.bam.mbc.common.theme.MonkeyTheme
import pm.bam.mbc.compose.ShowRow
import pm.bam.mbc.domain.models.Show
import pm.bam.mbc.feature.artists.ui.artist.ArtistViewModel.ArtistScreenData

@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun ArtistScreen(
    artistId: Long,
    onBack: () -> Unit,
    onViewShow: (showId: Long) -> Unit,
    goToWeb: (url: String, title: String) -> Unit,
    viewModel: ArtistViewModel = koinViewModel<ArtistViewModel>()
) {
    viewModel.loadArtistDetails(artistId)

    val data = viewModel.uiState.collectAsStateWithLifecycleFix()

    val onRetry: () -> Unit = { viewModel.reloadArtists(artistId) }

    ScreenScaffold(
        data = data.value,
        onBack = onBack,
        onViewShow = onViewShow,
        goToWeb = goToWeb,
        onRetry = onRetry
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenScaffold(
    data: ArtistScreenData,
    onBack: () -> Unit,
    onViewShow: (showId: Long) -> Unit,
    goToWeb: (url: String, showTitle: String) -> Unit,
    onRetry: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val title = when (data) {
        ArtistScreenData.Loading, ArtistScreenData.Error -> stringResource(Res.string.artists_screen_loading_label)
        is ArtistScreenData.Data -> data.artist.name
    }

    MonkeyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        modifier = Modifier.testTag(TopAppBarTag),
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        title = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) },
                        navigationIcon = {
                            IconButton(
                                modifier = Modifier.testTag(TopAppNavBarTag),
                                onClick = { onBack() }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(Res.string.artists_screen_navigation_back_button)
                                )
                            }
                        },
                        scrollBehavior = scrollBehavior,
                    )
                },
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
            ) { innerPadding: PaddingValues ->
                when (data) {
                    ArtistScreenData.Loading -> CircularProgressIndicator(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                            .testTag(LoadingDataTag)
                    )

                    ArtistScreenData.Error -> {
                        val message = stringResource(Res.string.artists_screen_data_loading_error_msg)
                        val actionLabel = stringResource(Res.string.artists_screen_data_loading_error_retry)

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

                    is ArtistScreenData.Data -> ArtistDetails(Modifier.padding(innerPadding), data, onViewShow, goToWeb)
                }
            }
        }
    }
}


@Composable
private fun ArtistDetails(
    modifier: Modifier,
    data: ArtistScreenData.Data,
    onViewShow: (showId: Long) -> Unit,
    goToWeb: (url: String, showTitle: String) -> Unit,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .testTag(ArtistTag),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = MonkeyCustomTheme.spacing.large, top = MonkeyCustomTheme.spacing.large, bottom = MonkeyCustomTheme.spacing.large)
                .testTag(ArtistDetailsTag)
        ) {
            AsyncImage(
                model = data.artist.images.firstOrNull(),
                contentDescription = stringResource(Res.string.artists_screen_artists_image_content_description, data.artist.name),
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
                        .testTag(ArtistDetailsTitleTag),
                    textAlign = TextAlign.Start,
                    text = data.artist.name,
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
            text = data.artist.description
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MonkeyCustomTheme.spacing.large),
            textAlign = TextAlign.Start,
            text = stringResource(Res.string.artists_screen_shows_label),
            style = MaterialTheme.typography.titleLarge,
        )

        HorizontalDivider()

        data.shows.forEach { show ->
            ShowRow(
                modifier = Modifier.testTag(ArtistScreenArtistRowTag.plus(show.id)),
                show = show,
                onShowSelected = onViewShow
            )
        }
    }
}


internal const val TopAppBarTag = "TopAppBarTag"
internal const val TopAppNavBarTag = "TopAppNavBarTag"
internal const val LoadingDataTag = "LoadingDataTag"

internal const val ArtistDetailsTag = "ArtistDetailsTag"
internal const val ArtistDetailsTitleTag = "ArtistDetailsTitleTag"

internal const val ArtistTag = "ArtistTagTag"
internal const val ArtistScreenArtistRowTag = "ArtistScreenArtistRowTag"