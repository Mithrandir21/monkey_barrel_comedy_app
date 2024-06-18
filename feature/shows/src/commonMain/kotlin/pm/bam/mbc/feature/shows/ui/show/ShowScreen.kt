package pm.bam.mbc.feature.shows.ui.show

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import monkeybarrelcomey.common.generated.resources.image_placeholder
import monkeybarrelcomey.feature.shows.generated.resources.Res
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_data_loading_error_msg
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_data_loading_error_retry
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_loading_label
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_navigation_back_button
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_show_dates_label
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_show_image_content_description
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_show_more_dates_label
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_show_performers_label
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_show_venues_label_plurals
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pm.bam.mbc.common.collectAsStateWithLifecycleFix
import pm.bam.mbc.compose.ArtistRow
import pm.bam.mbc.compose.ShowScheduleRow
import pm.bam.mbc.compose.ShowTags
import pm.bam.mbc.compose.theme.MonkeyCustomTheme
import pm.bam.mbc.compose.theme.MonkeyTheme
import pm.bam.mbc.domain.models.Show
import pm.bam.mbc.feature.shows.ui.show.ShowViewModel.ShowScreenData


internal const val SHOW_SCHEDULE_LIMIT = 3

@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun ShowScreen(
    showId: Long,
    onBack: () -> Unit,
    goToArtists: (artistId: Long) -> Unit,
    goToWeb: (url: String, showTitle: String) -> Unit,
    goToSchedules: (showId: Long) -> Unit,
    viewModel: ShowViewModel = koinViewModel<ShowViewModel>()
) {
    viewModel.loadShowDetails(showId)

    val data = viewModel.uiState.collectAsStateWithLifecycleFix()

    val onRetry: () -> Unit = { viewModel.reloadShow(showId) }

    ScreenScaffold(
        data = data.value,
        onBack = onBack,
        goToArtists = goToArtists,
        goToWeb = goToWeb,
        goToSchedules = goToSchedules,
        onRetry = onRetry
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenScaffold(
    data: ShowScreenData,
    onBack: () -> Unit,
    goToArtists: (artistId: Long) -> Unit,
    goToWeb: (url: String, showTitle: String) -> Unit,
    goToSchedules: (showId: Long) -> Unit,
    onRetry: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val title = when (data) {
        ShowScreenData.Loading, ShowScreenData.Error -> stringResource(Res.string.show_screen_loading_label)
        is ShowScreenData.Success -> data.show.name
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
                                    contentDescription = stringResource(Res.string.show_screen_navigation_back_button)
                                )
                            }
                        },
                        scrollBehavior = scrollBehavior,
                    )
                },
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
            ) { innerPadding: PaddingValues ->
                when (data) {
                    ShowScreenData.Loading -> CircularProgressIndicator(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                            .testTag(LoadingDataTag)
                    )

                    ShowScreenData.Error -> {
                        val message = stringResource(Res.string.show_screen_data_loading_error_msg)
                        val actionLabel = stringResource(Res.string.show_screen_data_loading_error_retry)

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

                    is ShowScreenData.Success -> ShowDetails(Modifier.padding(innerPadding), data, goToArtists, goToWeb, goToSchedules)
                }
            }
        }
    }
}


@Composable
private fun ShowDetails(
    modifier: Modifier,
    data: ShowScreenData.Success,
    goToArtists: (artistId: Long) -> Unit,
    goToWeb: (url: String, showTitle: String) -> Unit,
    goToSchedules: (showId: Long) -> Unit,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .testTag(ShowTag),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = MonkeyCustomTheme.spacing.large, top = MonkeyCustomTheme.spacing.large, bottom = MonkeyCustomTheme.spacing.large)
                .testTag(ShowDetailsTag)
        ) {
            AsyncImage(
                model = data.show.images.firstOrNull(),
                contentDescription = stringResource(Res.string.show_screen_show_image_content_description, data.show.name),
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
                        .testTag(ShowDetailsTitleTag),
                    textAlign = TextAlign.Start,
                    text = data.show.name,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                val showVenues = data.show.schedule.distinctBy { it.venue }.map { it.venue }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MonkeyCustomTheme.spacing.small),
                    textAlign = TextAlign.Start,
                    text = pluralStringResource(Res.plurals.show_screen_show_venues_label_plurals, showVenues.size, showVenues.joinToString(", ")),
                )
            }
        }

        ShowTags(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MonkeyCustomTheme.spacing.large),
            show = data.show
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MonkeyCustomTheme.spacing.large),
            textAlign = TextAlign.Start,
            text = data.show.description
        )


        var tabIndex by remember { mutableStateOf(0) }
        val tabs = listOf(
            stringResource(Res.string.show_screen_show_dates_label),
            stringResource(Res.string.show_screen_show_performers_label)
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            TabRow(selectedTabIndex = tabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(text = { Text(title) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                }
            }
            when (tabIndex) {
                0 -> showSchedule(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    show = data.show,
                    goToSchedules = goToSchedules
                )

                1 -> data.artists.forEach { artist ->
                    ArtistRow(
                        modifier = Modifier.testTag(ShowScreenArtistRowTag.plus(artist.id)),
                        artist = artist,
                        onViewArtist = goToArtists
                    )
                }
            }
        }
    }
}

@Composable
private fun showSchedule(
    modifier: Modifier = Modifier,
    show: Show,
    goToSchedules: (showId: Long) -> Unit
) {
    show.schedule.take(SHOW_SCHEDULE_LIMIT).forEach {
        ShowScheduleRow(
            modifier = Modifier.testTag(ShowDetailsTag),
            show = show,
            showSchedule = it,
            onShowSelected = { /* TODO: Implement */ }
        )
    }

    if (show.schedule.size > SHOW_SCHEDULE_LIMIT) {
        FilledTonalButton(
            modifier = modifier
                .wrapContentSize()
                .padding(MonkeyCustomTheme.spacing.large),
            onClick = { goToSchedules(show.id) }
        ) {
            Text(
                text = stringResource(Res.string.show_screen_show_more_dates_label),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


internal const val TopAppBarTag = "TopAppBarTag"
internal const val TopAppNavBarTag = "TopAppNavBarTag"
internal const val LoadingDataTag = "LoadingDataTag"

internal const val ShowDetailsTag = "ShowDetailsTag"
internal const val ShowDetailsTitleTag = "ShowDetailsTitleTag"

internal const val ShowTag = "ShowTagTag"
internal const val ShowScreenArtistRowTag = "ShowScreenArtistRowTag"