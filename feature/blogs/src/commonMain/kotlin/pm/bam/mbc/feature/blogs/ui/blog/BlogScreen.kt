package pm.bam.mbc.feature.blogs.ui.blog

import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun BlogScreen(
    onBack: () -> Unit,
    onViewBlogPost: (blogPostId: Long) -> Unit,
    viewModel: BlogViewModel = koinViewModel<BlogViewModel>()
) {

}