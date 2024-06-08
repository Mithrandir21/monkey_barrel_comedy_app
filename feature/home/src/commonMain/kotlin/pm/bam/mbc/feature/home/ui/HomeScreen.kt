package pm.bam.mbc.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import monkeybarrelcomey.feature.home.generated.resources.Res
import monkeybarrelcomey.feature.home.generated.resources.artists
import monkeybarrelcomey.feature.home.generated.resources.blog
import monkeybarrelcomey.feature.home.generated.resources.home_screen_data_card_artists_title
import monkeybarrelcomey.feature.home.generated.resources.home_screen_data_card_blogs_title
import monkeybarrelcomey.feature.home.generated.resources.home_screen_data_card_comedy_shows_title
import monkeybarrelcomey.feature.home.generated.resources.home_screen_data_card_podcast_episodes_title
import monkeybarrelcomey.feature.home.generated.resources.home_screen_data_loading_error_msg
import monkeybarrelcomey.feature.home.generated.resources.home_screen_data_loading_error_retry
import monkeybarrelcomey.feature.home.generated.resources.home_screen_show_section_title_upcoming_shows
import monkeybarrelcomey.feature.home.generated.resources.microphone
import monkeybarrelcomey.feature.home.generated.resources.podcast
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pm.bam.mbc.common.collectAsStateWithLifecycleFix
import pm.bam.mbc.compose.ShowRow
import pm.bam.mbc.compose.theme.MonkeyCustomTheme
import pm.bam.mbc.compose.theme.MonkeyTheme

@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun HomeScreen(
    onViewShow: (showId: Long) -> Unit,
    goToShows: () -> Unit,
    goToArtists: () -> Unit,
    goToPodcasts: () -> Unit,
    goToBlog: () -> Unit,
    viewModel: HomeViewModel = koinViewModel<HomeViewModel>()
) {
    val data = viewModel.uiState.collectAsStateWithLifecycleFix()

    Screen(
        data = data.value,
        onViewShow = onViewShow,
        onViewShows = goToShows,
        onViewArtists = goToArtists,
        onViewPodcasts = goToPodcasts,
        onViewBlogs = goToBlog,
        onRetry = { viewModel.loadData() }
    )
}


@Composable
private fun Screen(
    data: HomeViewModel.HomeScreenData,
    onViewShow: (showId: Long) -> Unit,
    onViewShows: () -> Unit,
    onViewArtists: () -> Unit,
    onViewPodcasts: () -> Unit,
    onViewBlogs: () -> Unit,
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
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                ) { innerPadding: PaddingValues ->
                    when (data) {
                        HomeViewModel.HomeScreenData.Error -> {
                            val message = stringResource(Res.string.home_screen_data_loading_error_msg)
                            val actionLabel = stringResource(Res.string.home_screen_data_loading_error_retry)

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

                        HomeViewModel.HomeScreenData.Loading -> CircularProgressIndicator(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center)
                                .testTag(HomeScreenLoadingDataTag)
                        )

                        is HomeViewModel.HomeScreenData.Success -> ScreenData(
                            Modifier.padding(innerPadding),
                            data = data,
                            onViewShow = onViewShow,
                            onViewShows = onViewShows,
                            onViewArtists = onViewArtists,
                            onViewPodcasts = onViewPodcasts,
                            onViewBlogs = onViewBlogs
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun ScreenData(
    modifier: Modifier = Modifier,
    data: HomeViewModel.HomeScreenData.Success,
    onViewShow: (showId: Long) -> Unit,
    onViewShows: () -> Unit,
    onViewArtists: () -> Unit,
    onViewPodcasts: () -> Unit,
    onViewBlogs: () -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        content = {
            if (data.topUpcomingShows.isNotEmpty()) {
                item { SectionHeader(stringResource(Res.string.home_screen_show_section_title_upcoming_shows)) }

                items(data.topUpcomingShows.size) { index ->
                    ShowRow(
                        modifier = Modifier.testTag(HomeScreenShowRowTag.plus(data.topUpcomingShows[index].id)),
                        show = data.topUpcomingShows[index],
                        onShowSelected = onViewShow
                    )
                }
            }

            item {

                HorizontalDivider()

                MainCards(
                    onViewShows = onViewShows,
                    onViewArtists = onViewArtists,
                    onViewPodcasts = onViewPodcasts,
                    onViewBlogs = onViewBlogs
                )
            }
        }
    )
}


@Composable
private fun SectionHeader(text: String) {
    Box(
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(MonkeyCustomTheme.spacing.medium),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}


@Composable
private fun MainCards(
    onViewShows: () -> Unit,
    onViewArtists: () -> Unit,
    onViewPodcasts: () -> Unit,
    onViewBlogs: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            HomeCard(
                modifier = Modifier.weight(1f),
                title = stringResource(Res.string.home_screen_data_card_comedy_shows_title),
                onClick = { onViewShows() },
                backgroundDrawableRes = Res.drawable.microphone
            )

            HomeCard(
                modifier = Modifier.weight(1f),
                title = stringResource(Res.string.home_screen_data_card_podcast_episodes_title),
                onClick = { onViewPodcasts() },
                backgroundDrawableRes = Res.drawable.podcast
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            HomeCard(
                modifier = Modifier.weight(1f),
                title = stringResource(Res.string.home_screen_data_card_artists_title),
                onClick = { onViewArtists() },
                backgroundDrawableRes = Res.drawable.artists
            )

            HomeCard(
                modifier = Modifier.weight(1f),
                title = stringResource(Res.string.home_screen_data_card_blogs_title),
                onClick = { onViewBlogs() },
                backgroundDrawableRes = Res.drawable.blog
            )
        }
    }
}


@Composable
private fun HomeCard(
    modifier: Modifier,
    title: String,
    onClick: () -> Unit,
    backgroundDrawableRes: DrawableResource
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(MonkeyCustomTheme.spacing.medium)
            .clip(RoundedCornerShape(MonkeyCustomTheme.spacing.medium))
            .clickable(onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(backgroundDrawableRes),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = Color.Gray, blendMode = BlendMode.Lighten)
            )
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(MonkeyCustomTheme.spacing.medium),
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black
            )
        }
    }
}

internal const val HomeScreenLoadingDataTag = "HomeScreenLoadingDataTag"

internal const val HomeScreenShowRowTag = "HomeScreenShowRowTag"