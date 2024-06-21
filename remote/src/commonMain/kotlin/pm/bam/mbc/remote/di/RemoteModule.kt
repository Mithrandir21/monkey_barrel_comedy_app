package pm.bam.mbc.remote.di

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.logging.LogLevel
import io.github.jan.supabase.postgrest.Postgrest
import org.koin.dsl.module
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.di.LoggingModule
import pm.bam.mbc.logging.info
import pm.bam.mbc.remote.datasources.RemoteArtistDataSource
import pm.bam.mbc.remote.datasources.RemoteArtistDataSourceImpl
import pm.bam.mbc.remote.datasources.RemoteBlogDataSource
import pm.bam.mbc.remote.datasources.RemoteBlogDataSourceImpl
import pm.bam.mbc.remote.datasources.RemoteMerchDataSource
import pm.bam.mbc.remote.datasources.RemoteMerchDataSourceImpl
import pm.bam.mbc.remote.datasources.RemoteNewsDataSource
import pm.bam.mbc.remote.datasources.RemoteNewsDataSourceImpl
import pm.bam.mbc.remote.datasources.RemotePodcastDataSource
import pm.bam.mbc.remote.datasources.RemotePodcastDataSourceImpl
import pm.bam.mbc.remote.datasources.RemoteShowsDataSource
import pm.bam.mbc.remote.datasources.RemoteShowsDataSourceImpl
import pm.bam.mbc.remote.secrets.Secrets
import pm.bam.mbc.remote.secrets.getSecrets

val remoteModule = module {
    includes(LoggingModule)

    single<Secrets> { getSecrets() }
    single<SupabaseClient> {

        val secrets = get<Secrets>()
        val logger = get<Logger>()

        createSupabaseClient(
            supabaseUrl = secrets.supaBaseUrl,
            supabaseKey = secrets.supaBaseKey
        ) {
            info(logger) { "Supabase client created" }

            defaultLogLevel = LogLevel.DEBUG

            install(Postgrest)

            info(logger) { "Supabase client Configured" }
        }
    }

    single<RemoteArtistDataSource> { RemoteArtistDataSourceImpl(get(), get()) }
    single<RemoteBlogDataSource> { RemoteBlogDataSourceImpl() }
    single<RemotePodcastDataSource> { RemotePodcastDataSourceImpl(get(), get()) }
    single<RemoteShowsDataSource> { RemoteShowsDataSourceImpl(get(), get()) }
    single<RemoteNewsDataSource> { RemoteNewsDataSourceImpl(get(), get()) }
    single<RemoteMerchDataSource> { RemoteMerchDataSourceImpl(get(), get()) }
}