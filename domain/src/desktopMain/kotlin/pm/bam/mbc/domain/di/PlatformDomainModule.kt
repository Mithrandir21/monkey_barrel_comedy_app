package pm.bam.mbc.domain.di

import org.koin.core.module.Module
import org.koin.dsl.module
import pm.bam.mbc.domain.db.DriverFactory


actual val platformModule: Module = module {
    single<DriverFactory> { DriverFactory() }
}