package pm.bam.mbc.common.datetime.formatting

import kotlinx.datetime.LocalDateTime

interface DateTimeFormatter {

    fun formatDateTimesFromTo(from: LocalDateTime, to: LocalDateTime): String

}