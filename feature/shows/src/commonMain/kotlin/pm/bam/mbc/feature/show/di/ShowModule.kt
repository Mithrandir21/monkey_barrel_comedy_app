package pm.bam.mbc.feature.show.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pm.bam.mbc.domain.di.domainModule
import pm.bam.mbc.domain.di.domainPlatformModule
import pm.bam.mbc.feature.show.ui.ShowViewModel
import pm.bam.mbc.logging.di.LoggingModule

val ShowModule = module {
    includes(LoggingModule, domainModule, domainPlatformModule)

    viewModel<ShowViewModel> { ShowViewModel(get(), get(), get()) }
}