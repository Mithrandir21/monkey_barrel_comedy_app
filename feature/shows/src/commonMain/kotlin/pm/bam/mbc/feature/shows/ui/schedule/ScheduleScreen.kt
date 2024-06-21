package pm.bam.mbc.feature.shows.ui.schedule

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import monkeybarrelcomey.feature.shows.generated.resources.Res
import monkeybarrelcomey.feature.shows.generated.resources.show_schedule_screen_empty_label
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_data_loading_error_msg
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_data_loading_error_retry
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_loading_label
import monkeybarrelcomey.feature.shows.generated.resources.show_screen_navigation_back_button
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pm.bam.mbc.common.collectAsStateWithLifecycleFix
import pm.bam.mbc.compose.ShowScheduleRow
import pm.bam.mbc.compose.theme.MonkeyTheme
import pm.bam.mbc.feature.shows.ui.schedule.ScheduleViewModel.ScheduleScreenData
import pm.bam.mbc.feature.shows.ui.show.LoadingDataTag
import pm.bam.mbc.feature.shows.ui.show.ShowDetailsTag
import pm.bam.mbc.feature.shows.ui.show.TopAppBarTag
import pm.bam.mbc.feature.shows.ui.show.TopAppNavBarTag

@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun ScheduleScreen(
    showId: Long,
    onBack: () -> Unit,
    goToWeb: (url: String, title: String) -> Unit,
    viewModel: ScheduleViewModel = koinViewModel<ScheduleViewModel>()
) {
    viewModel.loadShowSchedule(showId)

    val data = viewModel.uiState.collectAsStateWithLifecycleFix()

    val onRetry = { viewModel.loadShowSchedule(showId) }

    ScreenData(
        data = data.value,
        onBack = onBack,
        goToWeb = goToWeb,
        onRetry = onRetry
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenData(
    data: ScheduleScreenData,
    onBack: () -> Unit,
    goToWeb: (url: String, title: String) -> Unit,
    onRetry: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val title = when (data) {
        ScheduleScreenData.Loading, ScheduleScreenData.Error, ScheduleScreenData.Empty -> stringResource(Res.string.show_screen_loading_label)
        is ScheduleScreenData.Schedule -> data.show.name
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
                    ScheduleScreenData.Loading -> CircularProgressIndicator(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                            .testTag(LoadingDataTag)
                    )

                    ScheduleScreenData.Error -> {
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

                    ScheduleScreenData.Empty -> Text(
                        modifier = Modifier.padding(innerPadding)
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center),
                        text = stringResource(Res.string.show_schedule_screen_empty_label)
                    )

                    is ScheduleScreenData.Schedule -> Schedules(
                        modifier = Modifier.padding(innerPadding),
                        data = data,
                        goToWeb = goToWeb
                    )
                }
            }
        }
    }
}

@Composable
private fun Schedules(
    modifier: Modifier = Modifier,
    data: ScheduleScreenData.Schedule,
    goToWeb: (url: String, title: String) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(data.schedule.size) { index ->
            ShowScheduleRow(
                modifier = Modifier.testTag(ShowDetailsTag),
                show = data.show,
                showSchedule = data.schedule[index],
                onShowSelected = { goToWeb(data.show.url, data.show.name) }
            )
        }
    }
}