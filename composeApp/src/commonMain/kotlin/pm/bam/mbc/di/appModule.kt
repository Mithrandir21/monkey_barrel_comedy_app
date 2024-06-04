package pm.bam.mbc.di

import pm.bam.mbc.feature.artists.di.ArtistsModule
import pm.bam.mbc.feature.home.di.HomeModule
import pm.bam.mbc.feature.podcasts.di.PodcastsModule
import pm.bam.mbc.feature.show.di.ShowModule

fun appModule() = listOf(
    HomeModule, ShowModule, ArtistsModule, PodcastsModule
)