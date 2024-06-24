package pm.bam.mbc.di

import pm.bam.mbc.feature.artists.di.ArtistsModule
import pm.bam.mbc.feature.blogs.di.BlogsModule
import pm.bam.mbc.feature.home.di.HomeModule
import pm.bam.mbc.feature.merch.di.MerchModule
import pm.bam.mbc.feature.news.di.NewsModule
import pm.bam.mbc.feature.podcasts.di.PodcastsModule
import pm.bam.mbc.feature.shows.di.ShowsModule
import pm.bam.mbc.logging.di.LoggingModule

fun appModule() = listOf(
    LoggingModule, HomeModule, NewsModule, ShowsModule, BlogsModule, ArtistsModule, PodcastsModule, MerchModule
)