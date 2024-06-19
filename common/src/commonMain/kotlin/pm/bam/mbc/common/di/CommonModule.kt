package pm.bam.mbc.common.di

import kotlinx.serialization.json.Json
import org.koin.dsl.module
import pm.bam.mbc.common.datetime.formatting.DateTimeFormatter
import pm.bam.mbc.common.datetime.formatting.DateTimeFormatterImpl
import pm.bam.mbc.common.serializer.Serializer
import pm.bam.mbc.common.serializer.SerializerImpl

val commonModule = module {

    single<Json> {
        Json {
            prettyPrint = true
            isLenient = true
            encodeDefaults = true // Makes sure default field values are encoded
            ignoreUnknownKeys = true
        }
    }

    single<Serializer> { SerializerImpl(get()) }
    single<DateTimeFormatter> { DateTimeFormatterImpl() }

}