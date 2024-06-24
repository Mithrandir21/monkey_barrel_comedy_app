package pm.bam.mbc.logging.di

import org.koin.dsl.module
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.LoggerImpl
import pm.bam.mbc.logging.implementations.SimpleLoggingListener

val LoggingModule = module {
    single<Logger> { LoggerImpl(mutableSetOf(SimpleLoggingListener())) }
}