package pm.bam.mbc.logging

internal class LoggerImpl(private val loggers: MutableSet<LoggingInterface>) : Logger {

    /**
     * Method used to log a specific message and possible [Throwable].
     *
     * Note that, depending on implementation, zero or more [LoggingInterface]s might be called to pass on the message and possible [Throwable].
     *
     * @param level The level of logging that applies to the provided message and possible [Throwable].
     * @param tag The tag to use in logging.
     * @param throwable A specific [Throwable] associated with this log event. This could be a fatal exception to some journey, or simply informative.
     * @param messageProvider The lambda function that will provide the message to be logged.
     */
    override fun log(level: LogLevel, tag: String?, throwable: Throwable?, messageProvider: () -> String) =
        loggers.filter { it.isEnabled() }.forEach { it.onLog(level, messageProvider(), tag, throwable) }

    /**
     * A fatal [Throwable] has been thrown by the App. This happens when something catastrophic happens, something that cannot be recovered from.
     *
     * @param tag The tag to use in logging.
     * @param throwable The [Throwable] that has been thrown by the fatal event.
     */
    override fun fatalThrowable(throwable: Throwable, tag: String?) = loggers.filter { it.isEnabled() }.forEach { it.onFatalThrowable(tag, throwable) }

    /**
     * Add the provided [loggingInterface] to the [Set] of loggers that will receive log events.
     *
     * No exception will be thrown if the provided [loggingInterface] is already found in the existing set.
     */
    override fun addLoggerListener(loggingInterface: LoggingInterface) {
        loggers.add(loggingInterface)
    }
    /**
     * Remove the provided [loggingInterface] from the [Set] of loggers that will receive log events.
     *
     * No exception will be thrown if the provided [loggingInterface] is not found in the existing set.
     */
    override fun removeLoggerListener(loggingInterface: LoggingInterface) {
        loggers.remove(loggingInterface)
    }
}