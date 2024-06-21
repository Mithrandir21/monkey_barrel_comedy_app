package pm.bam.mbc.feature.merch.ui

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
import androidx.compose.ui.text.style.TextOverflow
import coil3.compose.AsyncImage
import monkeybarrelcomey.common.generated.resources.data_loading_error_msg
import monkeybarrelcomey.common.generated.resources.data_loading_error_retry
import monkeybarrelcomey.common.generated.resources.image_placeholder
import monkeybarrelcomey.common.generated.resources.merch_image_content_description
import monkeybarrelcomey.feature.merch.generated.resources.Res
import monkeybarrelcomey.feature.merch.generated.resources.merch_screen_merch_header_label
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pm.bam.mbc.common.collectAsStateWithLifecycleFix
import pm.bam.mbc.compose.BottomNavigation
import pm.bam.mbc.compose.NavigationBarConfig
import pm.bam.mbc.compose.theme.MonkeyCustomTheme
import pm.bam.mbc.compose.theme.MonkeyTheme
import pm.bam.mbc.domain.models.Merch
import pm.bam.mbc.feature.merch.ui.MerchViewModel.MerchScreenData
import pm.bam.mbc.feature.merch.ui.MerchViewModel.MerchScreenStatus
import monkeybarrelcomey.common.generated.resources.Res as CommonRes

@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun MerchScreen(
    bottomNavConfig: NavigationBarConfig,
    onViewMerchItem: (merchId: Long) -> Unit,
    viewModel: MerchViewModel = koinViewModel<MerchViewModel>()
) {
    val data = viewModel.uiState.collectAsStateWithLifecycleFix()

    val onRetry: () -> Unit = { viewModel.loadData() }

    Screen(
        data = data.value,
        onViewMerchItem = onViewMerchItem,
        bottomNavConfig = bottomNavConfig,
        onRetry = onRetry
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Screen(
    data: MerchScreenData,
    onViewMerchItem: (merchId: Long) -> Unit,
    bottomNavConfig: NavigationBarConfig,
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
                            modifier = Modifier.testTag(MerchScreenTopAppBarTag),
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text(
                                    text = stringResource(Res.string.merch_screen_merch_header_label),
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
                        columns = GridCells.Fixed(2),
                        content = {
                            items(data.news.size) { index ->
                                MerchItemCard(data.news[index], onViewMerchItem)
                            }
                        }
                    )

                    if (data.state == MerchScreenStatus.LOADING) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center)
                                .testTag(MerchScreenLoadingDataTag)
                        )
                    }
                }
            }

            if (data.state == MerchScreenStatus.ERROR) {
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
private fun MerchItemCard(
    merch: Merch,
    onViewMerchItem: (merchId: Long) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MonkeyCustomTheme.spacing.medium)
            .clip(RoundedCornerShape(MonkeyCustomTheme.spacing.medium))
            .clickable(onClick = { onViewMerchItem(merch.id) })
    ) {
        Column {
            AsyncImage(
                model = merch.images.firstOrNull(),
                contentDescription = stringResource(CommonRes.string.merch_image_content_description, merch.name),
                contentScale = ContentScale.Fit,
                error = painterResource(CommonRes.drawable.image_placeholder),
            )
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(MonkeyCustomTheme.spacing.medium),
                text = merch.name,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

internal const val MerchScreenTopAppBarTag = "MerchScreenTopAppBarTag"
internal const val MerchScreenTopAppNavBarTag = "MerchScreenTopAppNavBarTag"
internal const val MerchScreenLoadingDataTag = "MerchScreenLoadingDataTag"