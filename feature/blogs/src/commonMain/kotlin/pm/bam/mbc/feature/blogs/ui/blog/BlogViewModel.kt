package pm.bam.mbc.feature.blogs.ui.blog

import androidx.lifecycle.ViewModel
import pm.bam.mbc.domain.repositories.blog.BlogRepository
import pm.bam.mbc.logging.Logger

internal class BlogViewModel(
    private val logger: Logger,
    private val blogRepository: BlogRepository
) : ViewModel() {

}