package pm.bam.mbc.feature.home.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pm.bam.mbc.common.di.commonModule
import pm.bam.mbc.domain.di.domainModule
import pm.bam.mbc.domain.di.domainPlatformModule
import pm.bam.mbc.feature.home.ui.HomeViewModel
import pm.bam.mbc.logging.di.LoggingModule

val HomeModule = module {
    includes(LoggingModule, domainModule, domainPlatformModule, commonModule)

    viewModel<HomeViewModel> { HomeViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
}