package pm.bam.mbc.testing

import io.github.aakira.napier.Napier
import pm.bam.mbc.logging.LogLevel
import pm.bam.mbc.logging.Logger
import pm.bam.mbc.logging.LoggingInterface


class TestingLoggingListener : Logger {

    override fun log(level: LogLevel, tag: String?, throwable: Throwable?, messageProvider: () -> String) =
        Napier.log(level.toNapierLevel(), tag, throwable, "$level - $tag - ${messageProvider.invoke()}")

    override fun fatalThrowable(throwable: Throwable, tag: String?) =
        Napier.log(io.github.aakira.napier.LogLevel.ERROR, tag, throwable, "Fatal Throwable")

    override fun addLoggerListener(loggingInterface: LoggingInterface) = Unit

    override fun removeLoggerListener(loggingInterface: LoggingInterface) = Unit
}

private fun LogLevel.toNapierLevel(): io.github.aakira.napier.LogLevel = when (this) {
    LogLevel.VERBOSE -> io.github.aakira.napier.LogLevel.VERBOSE
    LogLevel.DEBUG -> io.github.aakira.napier.LogLevel.DEBUG
    LogLevel.INFO -> io.github.aakira.napier.LogLevel.INFO
    LogLevel.WARN -> io.github.aakira.napier.LogLevel.WARNING
    LogLevel.ERROR -> io.github.aakira.napier.LogLevel.ERROR
    LogLevel.FATAL -> io.github.aakira.napier.LogLevel.ERROR
}
