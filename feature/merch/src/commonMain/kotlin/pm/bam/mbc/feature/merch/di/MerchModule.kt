package pm.bam.mbc.feature.merch.di

import org.koin.dsl.module
import pm.bam.mbc.common.di.commonModule
import pm.bam.mbc.domain.di.domainModule
import pm.bam.mbc.domain.di.domainPlatformModule
import pm.bam.mbc.feature.merch.ui.MerchViewModel
import pm.bam.mbc.logging.di.LoggingModule

val MerchModule = module {
    includes(LoggingModule, domainModule, domainPlatformModule, commonModule)

    single<MerchViewModel> { MerchViewModel(get(), get()) }
}