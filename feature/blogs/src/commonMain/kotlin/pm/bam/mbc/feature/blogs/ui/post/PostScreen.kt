package pm.bam.mbc.feature.blogs.ui.post

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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import monkeybarrelcomey.common.generated.resources.image_placeholder
import monkeybarrelcomey.feature.blogs.generated.resources.Res
import monkeybarrelcomey.feature.blogs.generated.resources.blog_screen_data_loading_error_msg
import monkeybarrelcomey.feature.blogs.generated.resources.blog_screen_data_loading_error_retry
import monkeybarrelcomey.feature.blogs.generated.resources.blog_screen_loading_label
import monkeybarrelcomey.feature.blogs.generated.resources.blog_screen_navigation_back_button
import monkeybarrelcomey.feature.blogs.generated.resources.blog_screen_podcast_image_content_description
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pm.bam.mbc.common.collectAsStateWithLifecycleFix
import pm.bam.mbc.compose.theme.MonkeyCustomTheme
import pm.bam.mbc.compose.theme.MonkeyTheme
import pm.bam.mbc.feature.blogs.ui.post.PostViewModel.BlogPostScreenData


@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun PostScreen(
    blogPostId: Long,
    onBack: () -> Unit,
    viewModel: PostViewModel = koinViewModel<PostViewModel>()
) {
    viewModel.loadBlogPostDetails(blogPostId)

    val data = viewModel.uiState.collectAsStateWithLifecycleFix()

    val onRetry: () -> Unit = { viewModel.reloadBlogPosts(blogPostId) }

    ScreenScaffold(
        data = data.value,
        onBack = onBack,
        onRetry = onRetry
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenScaffold(
    data: BlogPostScreenData,
    onBack: () -> Unit,
    onRetry: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val title = when (data) {
        BlogPostScreenData.Loading, BlogPostScreenData.Error -> stringResource(Res.string.blog_screen_loading_label)
        is BlogPostScreenData.Success -> data.blogPost.title
    }

    MonkeyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        modifier = Modifier.testTag(BlogPostScreenTopAppBarTag),
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        title = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) },
                        navigationIcon = {
                            IconButton(
                                modifier = Modifier.testTag(BlogPostScreenTopAppNavBarTag),
                                onClick = { onBack() }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(Res.string.blog_screen_navigation_back_button)
                                )
                            }
                        },
                        scrollBehavior = scrollBehavior,
                    )
                },
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
            ) { innerPadding: PaddingValues ->
                when (data) {
                    BlogPostScreenData.Loading -> CircularProgressIndicator(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                            .testTag(BlogPostScreenLoadingDataTag)
                    )

                    BlogPostScreenData.Error -> {
                        val message = stringResource(Res.string.blog_screen_data_loading_error_msg)
                        val actionLabel = stringResource(Res.string.blog_screen_data_loading_error_retry)

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

                    is BlogPostScreenData.Success -> BlogPostDetails(Modifier.padding(innerPadding), data)
                }
            }
        }
    }
}


@Composable
private fun BlogPostDetails(
    modifier: Modifier,
    data: BlogPostScreenData.Success
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .testTag(BlogPostScreenTag),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = MonkeyCustomTheme.spacing.large, top = MonkeyCustomTheme.spacing.large, bottom = MonkeyCustomTheme.spacing.large)
                .testTag(BlogPostScreenDetailsTag)
        ) {
            AsyncImage(
                model = data.blogPost.images.firstOrNull(),
                contentDescription = stringResource(Res.string.blog_screen_podcast_image_content_description, data.blogPost.title),
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
                        .testTag(BlogPostScreenDetailsTitleTag),
                    textAlign = TextAlign.Start,
                    text = data.blogPost.title,
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
            text = data.blogPost.content
        )
    }
}


internal const val BlogPostScreenTopAppBarTag = "BlogPostScreenTopAppBarTag"
internal const val BlogPostScreenTopAppNavBarTag = "BlogPostScreenTopAppNavBarTag"
internal const val BlogPostScreenLoadingDataTag = "BlogPostScreenLoadingDataTag"

internal const val BlogPostScreenDetailsTag = "BlogPostScreenDetailsTag"
internal const val BlogPostScreenDetailsTitleTag = "BlogPostScreenDetailsTitleTag"

internal const val BlogPostScreenTag = "BlogPostScreenTag"