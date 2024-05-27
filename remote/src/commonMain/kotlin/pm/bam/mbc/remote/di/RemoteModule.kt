package pm.bam.mbc.remote.di

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import org.koin.dsl.module
import pm.bam.mbc.remote.api.ExampleAPI
import pm.bam.mbc.remote.client.ClientProviderImpl
import pm.bam.mbc.remote.datasources.RemoteArtistDataSource
import pm.bam.mbc.remote.datasources.RemoteArtistDataSourceImpl
import pm.bam.mbc.remote.datasources.RemoteBlogDataSource
import pm.bam.mbc.remote.datasources.RemoteBlogDataSourceImpl
import pm.bam.mbc.remote.datasources.RemotePodcastDataSource
import pm.bam.mbc.remote.datasources.RemotePodcastDataSourceImpl
import pm.bam.mbc.remote.datasources.RemoteShowsDataSource
import pm.bam.mbc.remote.datasources.RemoteShowsDataSourceImpl
import pm.bam.mbc.remote.logic.RemoteBuildType
import pm.bam.mbc.remote.logic.getRemoteBuildUtil

val module = module {
    single<RemoteBuildType> { getRemoteBuildUtil().buildType() }
    single<HttpClient> { ClientProviderImpl(get()).client() }
    single<Ktorfit> { createKtorfit(get()) }

    single<ExampleAPI> { createExampleApi(get()) }

    single<RemoteArtistDataSource> { RemoteArtistDataSourceImpl() }
    single<RemoteBlogDataSource> { RemoteBlogDataSourceImpl() }
    single<RemotePodcastDataSource> { RemotePodcastDataSourceImpl() }
    single<RemoteShowsDataSource> { RemoteShowsDataSourceImpl() }
}

fun createKtorfit(client: HttpClient): Ktorfit = Ktorfit.Builder().httpClient(client).build()

fun createExampleApi(ktorfit: Ktorfit): ExampleAPI = ktorfit.create()