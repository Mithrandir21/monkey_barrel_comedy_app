package pm.bam.mbc.feature.artists.ui.artist

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
import monkeybarrelcomey.common.generated.resources.artist_image_content_description
import monkeybarrelcomey.common.generated.resources.data_loading_error_msg
import monkeybarrelcomey.common.generated.resources.data_loading_error_retry
import monkeybarrelcomey.common.generated.resources.image_placeholder
import monkeybarrelcomey.common.generated.resources.loading_label
import monkeybarrelcomey.common.generated.resources.navigation_back_button
import monkeybarrelcomey.feature.artists.generated.resources.Res
import monkeybarrelcomey.feature.artists.generated.resources.artists_screen_merch_label
import monkeybarrelcomey.feature.artists.generated.resources.artists_screen_shows_label
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pm.bam.mbc.common.collectAsStateWithLifecycleFix
import pm.bam.mbc.common.datetime.formatting.DateTimeFormatter
import pm.bam.mbc.compose.MerchRow
import pm.bam.mbc.compose.ShowRow
import pm.bam.mbc.compose.theme.MonkeyCustomTheme
import pm.bam.mbc.compose.theme.MonkeyTheme
import pm.bam.mbc.feature.artists.ui.artist.ArtistViewModel.ArtistScreenData
import monkeybarrelcomey.common.generated.resources.Res as CommonRes

@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun ArtistScreen(
    artistId: Long,
    onBack: () -> Unit,
    onViewShow: (showId: Long) -> Unit,
    onViewMerch: (merchId: Long) -> Unit,
    goToWeb: (url: String, title: String) -> Unit,
    viewModel: ArtistViewModel = koinViewModel<ArtistViewModel>()
) {
    viewModel.loadArtistDetails(artistId)

    val data = viewModel.uiState.collectAsStateWithLifecycleFix()

    val onRetry: () -> Unit = { viewModel.reloadArtist(artistId) }

    ScreenScaffold(
        data = data.value,
        onBack = onBack,
        onViewShow = onViewShow,
        onViewMerch = onViewMerch,
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
    onViewMerch: (merchId: Long) -> Unit,
    goToWeb: (url: String, showTitle: String) -> Unit,
    onRetry: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val title = when (data) {
        ArtistScreenData.Loading, ArtistScreenData.Error -> stringResource(CommonRes.string.loading_label)
        is ArtistScreenData.Success -> data.artist.getFullName()
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
                                    contentDescription = stringResource(CommonRes.string.navigation_back_button)
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

                    is ArtistScreenData.Success -> ArtistDetails(Modifier.padding(innerPadding), data, onViewShow, onViewMerch, goToWeb)
                }
            }
        }
    }
}


@Composable
private fun ArtistDetails(
    modifier: Modifier,
    data: ArtistScreenData.Success,
    onViewShow: (showId: Long) -> Unit,
    onViewMerch: (merchId: Long) -> Unit,
    goToWeb: (url: String, showTitle: String) -> Unit,
    dateTimeFormatter: DateTimeFormatter = koinInject<DateTimeFormatter>()
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
                contentDescription = stringResource(CommonRes.string.artist_image_content_description, data.artist.getFullName()),
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
                    text = data.artist.getFullName(),
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

        var tabIndex by remember { mutableStateOf(0) }
        val tabs = listOf(
            stringResource(Res.string.artists_screen_shows_label) to TabType.Shows,
            stringResource(Res.string.artists_screen_merch_label) to TabType.Merch
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            TabRow(selectedTabIndex = tabIndex) {
                tabs.forEachIndexed { index, tab ->
                    Tab(text = { Text(tab.first) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                }
            }
            when (tabs[tabIndex].second) {
                TabType.Shows -> data.shows.forEach { show ->
                    ShowRow(
                        modifier = Modifier.testTag(ArtistScreenArtistRowTag.plus(show.id)),
                        show = show,
                        onShowSelected = onViewShow,
                        dateTimeFormatter = dateTimeFormatter,
                    )
                }

                TabType.Merch -> data.merch.forEach { merch ->
                    MerchRow(
                        modifier = Modifier.testTag(ArtistScreenMerchRowTag.plus(merch.id)),
                        merch = merch,
                        onMerchSelected = onViewMerch
                    )
                }
            }
        }
    }
}

private enum class TabType {
    Shows,
    Merch
}


internal const val TopAppBarTag = "TopAppBarTag"
internal const val TopAppNavBarTag = "TopAppNavBarTag"
internal const val LoadingDataTag = "LoadingDataTag"

internal const val ArtistDetailsTag = "ArtistDetailsTag"
internal const val ArtistDetailsTitleTag = "ArtistDetailsTitleTag"

internal const val ArtistTag = "ArtistTagTag"
internal const val ArtistScreenArtistRowTag = "ArtistScreenArtistRowTag"
internal const val ArtistScreenMerchRowTag = "ArtistScreenMerchRowTag"