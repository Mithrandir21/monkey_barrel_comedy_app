package pm.bam.mbc.feature.news.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pm.bam.mbc.domain.di.domainModule
import pm.bam.mbc.domain.di.domainPlatformModule
import pm.bam.mbc.feature.news.ui.item.NewsItemViewModel
import pm.bam.mbc.feature.news.ui.news.NewsViewModel
import pm.bam.mbc.logging.di.LoggingModule

val NewsModule = module {
    includes(LoggingModule, domainModule, domainPlatformModule)

    viewModel<NewsItemViewModel> { NewsItemViewModel(get(), get(), get()) }
    viewModel<NewsViewModel> { NewsViewModel(get(), get()) }
}