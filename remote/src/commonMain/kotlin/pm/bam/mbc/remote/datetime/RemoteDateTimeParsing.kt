package pm.bam.mbc.remote.datetime

import kotlinx.datetime.LocalDateTime

internal interface RemoteDateTimeParsing {

    fun parseRemoteDatabaseDateTime(value: String): LocalDateTime

}