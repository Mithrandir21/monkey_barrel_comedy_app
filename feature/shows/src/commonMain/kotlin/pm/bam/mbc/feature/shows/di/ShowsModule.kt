package pm.bam.mbc.feature.shows.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pm.bam.mbc.domain.di.domainModule
import pm.bam.mbc.domain.di.domainPlatformModule
import pm.bam.mbc.feature.shows.ui.ShowsViewModel
import pm.bam.mbc.logging.di.LoggingModule

val ShowsModule = module {
    includes(LoggingModule, domainModule, domainPlatformModule)

    viewModel<ShowsViewModel> { ShowsViewModel(get(), get(), get()) }
}