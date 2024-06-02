package pm.bam.mbc.feature.artists.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pm.bam.mbc.domain.di.domainModule
import pm.bam.mbc.domain.di.domainPlatformModule
import pm.bam.mbc.feature.artists.ui.ArtistsViewModel
import pm.bam.mbc.logging.di.LoggingModule

val ArtistsModule = module {
    includes(LoggingModule, domainModule, domainPlatformModule)

    viewModel<ArtistsViewModel> { ArtistsViewModel(get(), get()) }
}