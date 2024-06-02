package pm.bam.mbc

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.binds
import org.koin.dsl.module
import pm.bam.mbc.feature.artist.di.ArtistModule
import pm.bam.mbc.feature.artists.di.ArtistsModule
import pm.bam.mbc.feature.home.di.HomeModule
import pm.bam.mbc.feature.shows.di.ShowsModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            // https://github.com/InsertKoinIO/koin/issues/1735
            // androidContext(this@App)
            modules(
                HomeModule,
                ShowsModule,
                ArtistsModule,
                ArtistModule,
                module {
                    single { this@App } binds arrayOf(Context::class, Application::class)
                })
        }
    }
}