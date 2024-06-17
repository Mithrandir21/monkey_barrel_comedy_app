package pm.bam.mbc.feature.shows.ui.shows

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import coil3.compose.AsyncImage
import monkeybarrelcomey.common.generated.resources.image_placeholder
import monkeybarrelcomey.feature.shows.generated.resources.Res
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_artists_image_content_description
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_data_loading_error_msg
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_data_loading_error_retry
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_search_filters_icon
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_show_venue_label
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_shows_label
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pm.bam.mbc.common.collectAsStateWithLifecycleFix
import pm.bam.mbc.compose.BottomNavigation
import pm.bam.mbc.compose.NavigationBarConfig
import pm.bam.mbc.compose.ShowTags
import pm.bam.mbc.compose.theme.MonkeyCustomTheme
import pm.bam.mbc.compose.theme.MonkeyTheme
import pm.bam.mbc.domain.models.Show
import pm.bam.mbc.domain.models.ShowSearchParameters
import pm.bam.mbc.feature.shows.ui.shows.ShowsViewModel.ShowsScreenData
import pm.bam.mbc.feature.shows.ui.shows.ShowsViewModel.ShowsScreenStatus.EMPTY
import pm.bam.mbc.feature.shows.ui.shows.ShowsViewModel.ShowsScreenStatus.ERROR
import pm.bam.mbc.feature.shows.ui.shows.ShowsViewModel.ShowsScreenStatus.LOADING

@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun ShowsScreen(
    bottomNavConfig: NavigationBarConfig,
    goToShow: (showId: Long) -> Unit,
    viewModel: ShowsViewModel = koinViewModel<ShowsViewModel>()
) {
    val data = viewModel.uiState.collectAsStateWithLifecycleFix()

    var showFilters by rememberSaveable { mutableStateOf(false) }
    var existingParameters by rememberSaveable(stateSaver = parametersSaver) { mutableStateOf(ShowSearchParameters()) }

    val onRetry: () -> Unit = { viewModel.searchShows(existingParameters) }

    val showsSearchConfig = ShowsSearchConfig(
        existingSearchParameters = existingParameters,
        showFilters = showFilters,
        onShowFiltersChanged = { newShowFilters ->
            showFilters = newShowFilters
            if (!newShowFilters) {
                viewModel.searchShows(existingParameters)
            }
        },
        onSortByChanged = { sortBy -> existingParameters = existingParameters.copy(sortBy = sortBy) },
        onPriceChanged = { from, to -> existingParameters = existingParameters.copy(priceRange = from to to) },
        onClearPrice = { existingParameters = existingParameters.copy(priceRange = null) },
        onVenuesChanged = { venue, selected ->
            existingParameters = if (selected) {
                existingParameters.copy(venues = existingParameters.venues + venue)
            } else {
                existingParameters.copy(venues = existingParameters.venues - venue)
            }
        },
        onCategoriesChanged = { category, selected ->
            existingParameters = if (selected) {
                existingParameters.copy(categories = existingParameters.categories + category)
            } else {
                existingParameters.copy(categories = existingParameters.categories - category)
            }
        },
        onExactMatch = { exactMatch -> existingParameters = existingParameters.copy(titleExact = exactMatch) }
    )

    Screen(
        data = data.value,
        bottomNavConfig = bottomNavConfig,
        searchConfig = showsSearchConfig,
        onViewShow = goToShow,
        onRetry = onRetry
    )
}

@Composable
private fun Screen(
    data: ShowsScreenData,
    bottomNavConfig: NavigationBarConfig,
    searchConfig: ShowsSearchConfig,
    onViewShow: (artistId: Long) -> Unit,
    onRetry: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    MonkeyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Scaffold(
                    topBar = { ScreenTopAppBar(searchConfig) },
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    bottomBar = { BottomNavigation(config = bottomNavConfig) }
                ) { innerPadding: PaddingValues ->
                    ScreenData(
                        innerPadding = innerPadding,
                        data = data,
                        searchConfig = searchConfig,
                        onViewShow = onViewShow
                    )
                }
            }

            if (data.state == ERROR) {
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
        }
    }
}


@Composable
private fun ScreenData(
    innerPadding: PaddingValues,
    data: ShowsScreenData,
    searchConfig: ShowsSearchConfig,
    onViewShow: (artistId: Long) -> Unit
) {
    if (data.state == EMPTY) {
        showEmpty(Modifier.padding(innerPadding), searchConfig)
    } else {
        LazyVerticalGrid(
            modifier = Modifier.padding(innerPadding),
            columns = GridCells.Fixed(2),
            content = {
                items(data.shows.size) { index ->
                    ShowCard(data.shows[index], onViewShow)
                }
            }
        )
    }

    if (data.state == LOADING) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
                .testTag(ArtistsScreenLoadingDataTag)
        )
    }

    SearchFilters(searchConfig)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenTopAppBar(searchConfig: ShowsSearchConfig) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    TopAppBar(
        modifier = Modifier.testTag(ArtistsScreenTopAppBarTag),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                text = stringResource(Res.string.show_screen_shows_label),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        },
        actions = {
            IconButton(onClick = { searchConfig.onShowFiltersChanged(!searchConfig.showFilters) }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(Res.string.show_screen_search_filters_icon)
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}


@Composable
private fun showEmpty(
    modifier: Modifier = Modifier,
    searchConfig: ShowsSearchConfig
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "No shows found",
            style = MaterialTheme.typography.bodyLarge
        )
        Button(
            modifier = Modifier.wrapContentSize(),
            onClick = { searchConfig.onShowFiltersChanged(true) }) {
            Text(text = "Adjust filters")
        }
    }
}


@Composable
private fun ShowCard(
    show: Show,
    onViewShow: (artistId: Long) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MonkeyCustomTheme.spacing.medium)
            .clip(RoundedCornerShape(MonkeyCustomTheme.spacing.medium))
            .clickable(onClick = { onViewShow(show.id) })
    ) {
        Column {
            AsyncImage(
                model = show.images.firstOrNull(),
                contentDescription = stringResource(Res.string.show_screen_artists_image_content_description, show.name),
                contentScale = ContentScale.Fit,
                error = painterResource(monkeybarrelcomey.common.generated.resources.Res.drawable.image_placeholder),
            )
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(MonkeyCustomTheme.spacing.medium),
                text = show.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MonkeyCustomTheme.spacing.medium),
                textAlign = TextAlign.Start,
                text = show.schedule.first().start.date.toString(),
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            ShowTags(
                modifier = Modifier.fillMaxWidth(),
                show = show
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MonkeyCustomTheme.spacing.medium),
                textAlign = TextAlign.Start,
                text = stringResource(Res.string.show_screen_show_venue_label, show.schedule.first().venue),
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

internal const val ArtistsScreenTopAppBarTag = "ArtistsScreenTopAppBarTag"
internal const val ArtistsScreenTopAppNavBarTag = "ArtistsScreenTopAppNavBarTag"
internal const val ArtistsScreenLoadingDataTag = "ArtistsScreenLoadingDataTag"

internal const val ArtistsScreenSearchFieldTag = "ArtistsScreenSearchFieldTag"
internal const val ArtistsScreenSearchFiltersIconTag = "ArtistsScreenSearchFiltersIconTag"
