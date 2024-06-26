package pm.bam.mbc

import android.app.Application
import android.content.Context
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.binds
import org.koin.dsl.module
import pm.bam.mbc.feature.artists.di.ArtistsModule
import pm.bam.mbc.feature.blogs.di.BlogsModule
import pm.bam.mbc.feature.home.di.HomeModule
import pm.bam.mbc.feature.merch.di.MerchModule
import pm.bam.mbc.feature.news.di.NewsModule
import pm.bam.mbc.feature.podcasts.di.PodcastsModule
import pm.bam.mbc.feature.shows.di.ShowsModule
import pm.bam.mbc.logging.di.LoggingModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            // https://github.com/InsertKoinIO/koin/issues/1735
            // androidContext(this@App)
            modules(
                LoggingModule,
                HomeModule,
                NewsModule,
                ShowsModule,
                BlogsModule,
                ArtistsModule,
                PodcastsModule,
                MerchModule,
                module {
                    single { this@App } binds arrayOf(Context::class, Application::class)
                })
        }

        Napier.base(DebugAntilog())
    }
}