package pm.bam.mbc.remote.datetime

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

internal class RemoteDateTimeParsingImpl : RemoteDateTimeParsing {

    @OptIn(FormatStringsInDatetimeFormats::class)
    private var remoteDatabaseDateTimeFormatter: DateTimeFormat<LocalDateTime> = LocalDateTime.Format {
        byUnicodePattern("uuuu-MM-dd'T'HH:mm:ss+00:00")
    }

    override fun parseRemoteDatabaseDateTime(value: String): LocalDateTime = remoteDatabaseDateTimeFormatter.parse(value)

}