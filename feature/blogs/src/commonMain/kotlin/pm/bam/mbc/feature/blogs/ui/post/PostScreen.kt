package pm.bam.mbc.feature.blogs.ui.post

import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun PostScreen(
    blogPostId: Long,
    onBack: () -> Unit,
    onViewArtist: (artistId: Long) -> Unit,
    viewModel: PostViewModel = koinViewModel<PostViewModel>()
) {

}