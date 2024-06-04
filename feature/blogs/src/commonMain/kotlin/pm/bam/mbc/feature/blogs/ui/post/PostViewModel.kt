package pm.bam.mbc.feature.blogs.ui.post

import androidx.lifecycle.ViewModel
import pm.bam.mbc.domain.repositories.blog.BlogRepository
import pm.bam.mbc.logging.Logger

internal class PostViewModel(
    private val logger: Logger,
    private val blogRepository: BlogRepository
) : ViewModel() {

}