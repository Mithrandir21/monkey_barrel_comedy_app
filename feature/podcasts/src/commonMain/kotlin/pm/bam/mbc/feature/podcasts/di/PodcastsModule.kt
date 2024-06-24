package pm.bam.mbc.feature.podcasts.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pm.bam.mbc.common.di.commonModule
import pm.bam.mbc.domain.di.domainModule
import pm.bam.mbc.domain.di.domainPlatformModule
import pm.bam.mbc.feature.podcasts.ui.episode.PodcastEpisodeViewModel
import pm.bam.mbc.feature.podcasts.ui.podcast.PodcastViewModel
import pm.bam.mbc.feature.podcasts.ui.podcasts.PodcastsViewModel
import pm.bam.mbc.logging.di.LoggingModule

val PodcastsModule = module {
    includes(LoggingModule, domainModule, domainPlatformModule, commonModule)

    viewModel<PodcastsViewModel> { PodcastsViewModel(get(), get()) }
    viewModel<PodcastViewModel> { PodcastViewModel(get(), get()) }
    viewModel<PodcastEpisodeViewModel> { PodcastEpisodeViewModel(get(), get(), get(), get()) }
}