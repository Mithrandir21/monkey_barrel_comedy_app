package pm.bam.mbc.remote.di

import de.jensklingenberg.ktorfit.Ktorfit
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.logging.LogLevel
import io.github.jan.supabase.postgrest.Postgrest
import io.ktor.client.HttpClient
import org.koin.dsl.module
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.di.LoggingModule
import pm.bam.mbc.logging.info
import pm.bam.mbc.remote.api.ExampleAPI
import pm.bam.mbc.remote.client.ClientProviderImpl
import pm.bam.mbc.remote.datasources.RemoteArtistDataSource
import pm.bam.mbc.remote.datasources.RemoteArtistDataSourceImpl
import pm.bam.mbc.remote.datasources.RemoteBlogDataSource
import pm.bam.mbc.remote.datasources.RemoteBlogDataSourceImpl
import pm.bam.mbc.remote.datasources.RemoteNewsDataSource
import pm.bam.mbc.remote.datasources.RemoteNewsDataSourceImpl
import pm.bam.mbc.remote.datasources.RemotePodcastDataSource
import pm.bam.mbc.remote.datasources.RemotePodcastDataSourceImpl
import pm.bam.mbc.remote.datasources.RemoteShowsDataSource
import pm.bam.mbc.remote.datasources.RemoteShowsDataSourceImpl
import pm.bam.mbc.remote.datetime.RemoteDateTimeParsing
import pm.bam.mbc.remote.datetime.RemoteDateTimeParsingImpl
import pm.bam.mbc.remote.logic.RemoteBuildType
import pm.bam.mbc.remote.logic.getRemoteBuildUtil
import pm.bam.mbc.remote.secrets.Secrets
import pm.bam.mbc.remote.secrets.getSecrets

val remoteModule = module {
    includes(LoggingModule)

    single<RemoteBuildType> { getRemoteBuildUtil().buildType() }
    single<HttpClient> { ClientProviderImpl(get()).client() }
    single<Ktorfit> { createKtorfit(get()) }

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

    single<ExampleAPI> { createExampleApi(get()) }
    single<RemoteDateTimeParsing> { RemoteDateTimeParsingImpl() }

    single<RemoteArtistDataSource> { RemoteArtistDataSourceImpl(get(), get()) }
    single<RemoteBlogDataSource> { RemoteBlogDataSourceImpl() }
    single<RemotePodcastDataSource> { RemotePodcastDataSourceImpl(get(), get()) }
    single<RemoteShowsDataSource> { RemoteShowsDataSourceImpl(get(), get(), get()) }
    single<RemoteNewsDataSource> { RemoteNewsDataSourceImpl(get(), get()) }
}

fun createKtorfit(client: HttpClient): Ktorfit = Ktorfit.Builder().httpClient(client).build()

fun createExampleApi(ktorfit: Ktorfit): ExampleAPI = ktorfit.create()