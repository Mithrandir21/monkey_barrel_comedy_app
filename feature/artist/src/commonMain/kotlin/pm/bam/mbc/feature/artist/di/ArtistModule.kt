package pm.bam.mbc.feature.artist.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pm.bam.mbc.domain.di.domainModule
import pm.bam.mbc.domain.di.domainPlatformModule
import pm.bam.mbc.feature.artist.ui.ArtistViewModel
import pm.bam.mbc.logging.di.LoggingModule

val ArtistModule = module {
    includes(LoggingModule, domainModule, domainPlatformModule)

    viewModel<ArtistViewModel> { ArtistViewModel(get(), get(), get()) }
}