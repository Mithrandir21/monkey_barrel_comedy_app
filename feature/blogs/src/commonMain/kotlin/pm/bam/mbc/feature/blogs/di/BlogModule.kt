package pm.bam.mbc.feature.blogs.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pm.bam.mbc.domain.di.domainModule
import pm.bam.mbc.domain.di.domainPlatformModule
import pm.bam.mbc.feature.blogs.ui.blog.BlogViewModel
import pm.bam.mbc.feature.blogs.ui.post.PostViewModel
import pm.bam.mbc.logging.di.LoggingModule

val BlogsModule = module {
    includes(LoggingModule, domainModule, domainPlatformModule)

    viewModel<BlogViewModel> { BlogViewModel(get(), get()) }
    viewModel<PostViewModel> { PostViewModel(get(), get()) }
}