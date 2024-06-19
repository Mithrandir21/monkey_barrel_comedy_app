package pm.bam.mbc.common.datetime.formatting

import kotlinx.datetime.LocalDateTime

internal class DateTimeFormatterImpl : DateTimeFormatter {

    override fun formatDateTimesFromTo(from: LocalDateTime, to: LocalDateTime): String =
        "${getOrdinalIndicator(from.dayOfMonth)} " +
                "${from.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }} " +
                "${timeFormatAmPm(from)} - " +
                timeFormatAmPm(to)
}

private fun getOrdinalIndicator(day: Int): String =
    when (day) {
        1, 21, 31 -> "${day}st"
        2, 22 -> "${day}nd"
        3, 23 -> "${day}rd"
        else -> "${day}th"
    }

private fun timeFormatAmPm(localDateTime: LocalDateTime): String =
    "${localDateTime.hour}:${localDateTime.minute}".plus(if (localDateTime.hour < 12) "am" else "pm")