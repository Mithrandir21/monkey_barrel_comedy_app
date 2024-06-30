package pm.bam.mbc.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import monkeybarrelcomey.common.generated.resources.data_loading_error_msg
import monkeybarrelcomey.common.generated.resources.data_loading_error_retry
import monkeybarrelcomey.feature.home.generated.resources.Res
import monkeybarrelcomey.feature.home.generated.resources.blog
import monkeybarrelcomey.feature.home.generated.resources.home_screen_artists_section_title
import monkeybarrelcomey.feature.home.generated.resources.home_screen_data_card_blogs_title
import monkeybarrelcomey.feature.home.generated.resources.home_screen_news_section_title
import monkeybarrelcomey.feature.home.generated.resources.home_screen_title
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pm.bam.mbc.common.collectAsStateWithLifecycleFix
import pm.bam.mbc.common.datetime.formatting.DateTimeFormatter
import pm.bam.mbc.compose.ArtistRow
import pm.bam.mbc.compose.BottomNavigation
import pm.bam.mbc.compose.NavigationBarConfig
import pm.bam.mbc.compose.NewsRow
import pm.bam.mbc.compose.ShowRow
import pm.bam.mbc.compose.theme.MonkeyCustomTheme
import pm.bam.mbc.compose.theme.MonkeyTheme
import monkeybarrelcomey.common.generated.resources.Res as CommonRes

@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun HomeScreen(
    bottomNavConfig: NavigationBarConfig,
    goToNewsItem: (newsId: Long) -> Unit,
    goToNews: () -> Unit,
    onViewArtist: (artistId: Long) -> Unit,
    onViewShow: (showId: Long) -> Unit,
    goToBlog: () -> Unit,
    viewModel: HomeViewModel = koinViewModel<HomeViewModel>()
) {
    val data = viewModel.uiState.collectAsStateWithLifecycleFix()

    Screen(
        data = data.value,
        bottomNavConfig = bottomNavConfig,
        onViewNewsItem = goToNewsItem,
        onViewArtist = onViewArtist,
        onViewShow = onViewShow,
        onViewBlogs = goToBlog,
        onSignup = { viewModel.signUp() },
        onRetry = { viewModel.loadData() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Screen(
    data: HomeViewModel.HomeScreenData,
    bottomNavConfig: NavigationBarConfig,
    onViewNewsItem: (newsId: Long) -> Unit,
    onViewArtist: (artistId: Long) -> Unit,
    onViewShow: (showId: Long) -> Unit,
    onViewBlogs: () -> Unit,
    onSignup: () -> Unit,
    onRetry: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val snackbarHostState = remember { SnackbarHostState() }

    MonkeyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            modifier = Modifier.testTag(HomeScreenTopAppBarTag),
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = { Text(text = stringResource(Res.string.home_screen_title), maxLines = 2, overflow = TextOverflow.Ellipsis) },
                            scrollBehavior = scrollBehavior,
                        )
                    },
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    bottomBar = { BottomNavigation(config = bottomNavConfig) }
                ) { innerPadding: PaddingValues ->
                    when (data) {
                        HomeViewModel.HomeScreenData.Error -> {
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
                            onViewNewsItem = onViewNewsItem,
                            onViewArtist = onViewArtist,
                            onViewShow = onViewShow,
                            onViewBlogs = onViewBlogs,
                            onSignup = onSignup
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
    onViewNewsItem: (newsId: Long) -> Unit,
    onViewArtist: (artistId: Long) -> Unit,
    onViewShow: (showId: Long) -> Unit,
    onViewBlogs: () -> Unit,
    onSignup: () -> Unit,
    dateTimeFormatter: DateTimeFormatter = koinInject<DateTimeFormatter>()
) {
    LazyColumn(
        modifier = modifier,
        content = {
            item {
                SectionHeader("Signed In: ${data.userLoggedIn}")
                Button(onClick = onSignup) {
                    Text("Signup")
                }
            }

            if (data.topUpcomingShows.isNotEmpty()) {
                items(data.topUpcomingShows.size) { index ->
                    ShowRow(
                        modifier = Modifier.testTag(HomeScreenShowRowTag.plus(data.topUpcomingShows[index].id)),
                        show = data.topUpcomingShows[index],
                        onShowSelected = onViewShow,
                        dateTimeFormatter = dateTimeFormatter
                    )
                }
            }

            if (data.featuredArtist.isNotEmpty()) {
                item { SectionHeader(stringResource(Res.string.home_screen_artists_section_title)) }

                items(data.featuredArtist.size) { index ->
                    ArtistRow(
                        modifier = Modifier.testTag(HomeScreenArtistRowTag.plus(data.featuredArtist[index].id)),
                        artist = data.featuredArtist[index],
                        onViewArtist = onViewArtist
                    )
                }
            }

            if (data.news.isNotEmpty()) {
                item { SectionHeader(stringResource(Res.string.home_screen_news_section_title)) }

                items(data.news.size) { index ->
                    NewsRow(
                        modifier = Modifier.testTag(HomeScreenNewsRowTag.plus(data.news[index].id)),
                        news = data.news[index],
                        onViewNewsItem = onViewNewsItem
                    )
                }
            }

//            item {
//
//                HorizontalDivider()
//
//                HomeCard(
//                    modifier = Modifier.fillMaxWidth(),
//                    title = stringResource(Res.string.home_screen_data_card_blogs_title),
//                    onClick = { onViewBlogs() },
//                    backgroundDrawableRes = Res.drawable.blog
//                )
//            }
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

internal const val HomeScreenTopAppBarTag = "HomeScreenTopAppBarTag"

internal const val HomeScreenLoadingDataTag = "HomeScreenLoadingDataTag"

internal const val HomeScreenNewsRowTag = "HomeScreenNewsRowTag"
internal const val HomeScreenShowRowTag = "HomeScreenShowRowTag"
internal const val HomeScreenArtistRowTag = "HomeScreenArtistRowTag"