package pm.bam.mbc

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.binds
import org.koin.dsl.module
import pm.bam.mbc.feature.home.di.HomeModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            // https://github.com/InsertKoinIO/koin/issues/1735
            // androidContext(this@App)
            modules(
                HomeModule,
                module {
                    single { this@App } binds arrayOf(Context::class, Application::class)
                })
        }
    }
}