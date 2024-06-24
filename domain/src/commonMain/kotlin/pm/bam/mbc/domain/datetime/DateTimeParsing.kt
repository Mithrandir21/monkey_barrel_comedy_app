package pm.bam.mbc.domain.datetime

import kotlinx.datetime.LocalDateTime

internal interface DateTimeParsing {

    fun parseRemoteDatabaseDateTime(value: String): LocalDateTime

}