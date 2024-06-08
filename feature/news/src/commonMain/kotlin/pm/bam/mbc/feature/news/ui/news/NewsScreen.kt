package pm.bam.mbc.feature.news.ui.news

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import coil3.compose.AsyncImage
import monkeybarrelcomey.common.generated.resources.image_placeholder
import monkeybarrelcomey.feature.news.generated.resources.Res
import monkeybarrelcomey.feature.news.generated.resources.news_screen_data_loading_error_msg
import monkeybarrelcomey.feature.news.generated.resources.news_screen_data_loading_error_retry
import monkeybarrelcomey.feature.news.generated.resources.news_screen_navigation_back_button
import monkeybarrelcomey.feature.news.generated.resources.news_screen_news_header_label
import monkeybarrelcomey.feature.news.generated.resources.news_screen_news_image_content_description
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pm.bam.mbc.common.collectAsStateWithLifecycleFix
import pm.bam.mbc.compose.theme.MonkeyCustomTheme
import pm.bam.mbc.compose.theme.MonkeyTheme
import pm.bam.mbc.domain.models.News
import pm.bam.mbc.feature.news.ui.news.NewsViewModel.NewsScreenData
import pm.bam.mbc.feature.news.ui.news.NewsViewModel.NewsScreenStatus.ERROR
import pm.bam.mbc.feature.news.ui.news.NewsViewModel.NewsScreenStatus.LOADING

@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun NewsScreen(
    onBack: () -> Unit,
    onViewNewsItem: (newsId: Long) -> Unit,
    viewModel: NewsViewModel = koinViewModel<NewsViewModel>()
) {
    val data = viewModel.uiState.collectAsStateWithLifecycleFix()

    val onRetry: () -> Unit = { viewModel.loadData() }

    Screen(
        data = data.value,
        onViewNewsItem = onViewNewsItem,
        onBack = onBack,
        onRetry = onRetry
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Screen(
    data: NewsScreenData,
    onViewNewsItem: (newsId: Long) -> Unit,
    onBack: () -> Unit,
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
                            modifier = Modifier.testTag(NewsScreenTopAppBarTag),
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text(
                                    text = stringResource(Res.string.news_screen_news_header_label),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            navigationIcon = {
                                IconButton(
                                    modifier = Modifier.testTag(NewsScreenTopAppNavBarTag),
                                    onClick = { onBack() }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = stringResource(Res.string.news_screen_navigation_back_button)
                                    )
                                }
                            },
                            scrollBehavior = scrollBehavior,
                        )
                    },
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                ) { innerPadding: PaddingValues ->
                    LazyVerticalGrid(
                        modifier = Modifier.padding(innerPadding),
                        columns = GridCells.Fixed(2),
                        content = {
                            items(data.news.size) { index ->
                                NewsItemCard(data.news[index], onViewNewsItem)
                            }
                        }
                    )

                    if (data.state == LOADING) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center)
                                .testTag(NewsScreenLoadingDataTag)
                        )
                    }
                }
            }

            if (data.state == ERROR) {
                val message = stringResource(Res.string.news_screen_data_loading_error_msg)
                val actionLabel = stringResource(Res.string.news_screen_data_loading_error_retry)

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
private fun NewsItemCard(
    news: News,
    onViewNewsItem: (newsId: Long) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MonkeyCustomTheme.spacing.medium)
            .clip(RoundedCornerShape(MonkeyCustomTheme.spacing.medium))
            .clickable(onClick = { onViewNewsItem(news.id) })
    ) {
        Column {
            AsyncImage(
                model = news.images.firstOrNull(),
                contentDescription = stringResource(Res.string.news_screen_news_image_content_description, news.title),
                contentScale = ContentScale.Fit,
                error = painterResource(monkeybarrelcomey.common.generated.resources.Res.drawable.image_placeholder),
            )
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(MonkeyCustomTheme.spacing.medium),
                text = news.title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

internal const val NewsScreenTopAppBarTag = "NewsScreenTopAppBarTag"
internal const val NewsScreenTopAppNavBarTag = "NewsScreenTopAppNavBarTag"
internal const val NewsScreenLoadingDataTag = "NewsScreenLoadingDataTag"