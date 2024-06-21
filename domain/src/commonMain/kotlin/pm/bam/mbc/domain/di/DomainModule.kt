package pm.bam.mbc.domain.di

import org.koin.core.module.Module
import org.koin.dsl.module
import pm.bam.mbc.common.di.commonModule
import pm.bam.mbc.domain.Database
import pm.bam.mbc.domain.datetime.DateTimeParsing
import pm.bam.mbc.domain.datetime.DateTimeParsingImpl
import pm.bam.mbc.domain.db.DriverFactory
import pm.bam.mbc.domain.db.createDatabase
import pm.bam.mbc.domain.repositories.artist.ArtistRepository
import pm.bam.mbc.domain.repositories.artist.ArtistRepositoryImpl
import pm.bam.mbc.domain.repositories.blog.BlogRepository
import pm.bam.mbc.domain.repositories.blog.BlogRepositoryImpl
import pm.bam.mbc.domain.repositories.merch.MerchRepository
import pm.bam.mbc.domain.repositories.merch.MerchRepositoryImpl
import pm.bam.mbc.domain.repositories.news.NewsRepository
import pm.bam.mbc.domain.repositories.news.NewsRepositoryImpl
import pm.bam.mbc.domain.repositories.podcast.PodcastRepository
import pm.bam.mbc.domain.repositories.podcast.PodcastRepositoryImpl
import pm.bam.mbc.domain.repositories.shows.ShowsRepository
import pm.bam.mbc.domain.repositories.shows.ShowsRepositoryImpl
import pm.bam.mbc.logging.di.LoggingModule
import pm.bam.mbc.remote.di.remoteModule
import pmbammbcdomain.DatabaseArtistQueries
import pmbammbcdomain.DatabaseBlogPostQueries
import pmbammbcdomain.DatabaseMerchItemQueries
import pmbammbcdomain.DatabaseMerchQueries
import pmbammbcdomain.DatabaseNewsQueries
import pmbammbcdomain.DatabasePodcastEpisodeQueries
import pmbammbcdomain.DatabasePodcastQueries
import pmbammbcdomain.DatabaseShowQueries


expect val domainPlatformModule: Module

val domainModule = module {
    includes(LoggingModule, remoteModule, commonModule)

    single<Database> { createDatabase(get<DriverFactory>()) }
    single<DateTimeParsing> { DateTimeParsingImpl() }

    single<DatabaseNewsQueries> { get<Database>().databaseNewsQueries }
    single<DatabaseShowQueries> { get<Database>().databaseShowQueries }
    single<DatabaseArtistQueries> { get<Database>().databaseArtistQueries }
    single<DatabaseBlogPostQueries> { get<Database>().databaseBlogPostQueries }
    single<DatabasePodcastQueries> { get<Database>().databasePodcastQueries }
    single<DatabasePodcastEpisodeQueries> { get<Database>().databasePodcastEpisodeQueries }
    single<DatabaseMerchQueries> { get<Database>().databaseMerchQueries }
    single<DatabaseMerchItemQueries> { get<Database>().databaseMerchItemQueries }

    single<NewsRepository> { NewsRepositoryImpl(get(), get(), get()) }
    single<ArtistRepository> { ArtistRepositoryImpl(get(), get(), get()) }
    single<BlogRepository> { BlogRepositoryImpl(get(), get(), get()) }
    single<PodcastRepository> { PodcastRepositoryImpl(get(), get(), get(), get()) }
    single<ShowsRepository> { ShowsRepositoryImpl(get(), get(), get(), get()) }
    single<MerchRepository> { MerchRepositoryImpl(get(), get(), get(), get()) }
}