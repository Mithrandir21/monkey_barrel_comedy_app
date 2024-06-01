package pm.bam.mbc.feature.home.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pm.bam.mbc.domain.di.domainModule
import pm.bam.mbc.domain.di.platformModule
import pm.bam.mbc.feature.home.ui.HomeViewModel

val HomeModule = module {
    includes(domainModule, platformModule)

    viewModel { HomeViewModel(get()) }
}