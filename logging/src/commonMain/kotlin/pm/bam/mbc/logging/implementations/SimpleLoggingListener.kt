package pm.bam.mbc.logging.implementations

import io.github.aakira.napier.Napier
import pm.bam.mbc.logging.LogLevel
import pm.bam.mbc.logging.LoggingInterface

internal class SimpleLoggingListener : LoggingInterface {

    /** Returns a boolean indicating whether this [LoggingInterface] is enabled or not. */
    override fun isEnabled(): Boolean = true

    /** Tag for the interface used internally for reporting or identifying. */
    override fun getLoggerTag(): String = SimpleLoggingListener::class.simpleName ?: "SimpleLoggingListener"

    /**
     * Method called when some information needs logging.
     *
     * @param level The level of logging that applies to the provided message and possible [Throwable].
     * @param message The logged message.
     * @param tag The tag to use in logging.
     * @param throwable A specific [Throwable] associated with this log event. This could be a fatal exception to some journey, or simply informative.
     */
    override fun onLog(level: LogLevel, message: String, tag: String?, throwable: Throwable?) =
        Napier.log(level.toNapierLevel(), tag, throwable, message)

    /**
     * A fatal [Throwable] has been thrown by the App. This happens when something catastrophic happens, something that cannot be recovered from.
     *
     * @param tag The tag to use in logging.
     * @param throwable The [Throwable] that has been thrown by the fatal event.
     */
    override fun onFatalThrowable(tag: String?, throwable: Throwable) =
        Napier.log(io.github.aakira.napier.LogLevel.ERROR, tag, throwable, "Fatal crash")
}

private fun LogLevel.toNapierLevel(): io.github.aakira.napier.LogLevel = when (this) {
    LogLevel.VERBOSE -> io.github.aakira.napier.LogLevel.VERBOSE
    LogLevel.DEBUG -> io.github.aakira.napier.LogLevel.DEBUG
    LogLevel.INFO -> io.github.aakira.napier.LogLevel.INFO
    LogLevel.WARN -> io.github.aakira.napier.LogLevel.WARNING
    LogLevel.ERROR -> io.github.aakira.napier.LogLevel.ERROR
    LogLevel.FATAL -> io.github.aakira.napier.LogLevel.ERROR
}
