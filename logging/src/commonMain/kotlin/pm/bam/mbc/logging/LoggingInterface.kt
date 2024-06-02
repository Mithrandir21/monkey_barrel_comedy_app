package pm.bam.mbc.logging


/** Listener interface used to report messages and possible [Throwable] to implementing listeners. */
interface LoggingInterface {

    /** Returns a boolean indicating whether this [LoggingInterface] is enabled or not. */
    fun isEnabled(): Boolean

    /** Tag for the interface used internally for reporting or identifying. */
    fun getLoggerTag(): String

    /**
     * Method called when some information needs logging.
     *
     * @param level The level of logging that applies to the provided message and possible [Throwable].
     * @param message The logged message.
     * @param tag The tag to use in logging.
     * @param throwable A specific [Throwable] associated with this log event. This could be a fatal exception to some journey, or simply informative.
     *
     * @since 1.0
     */
    fun onLog(level: LogLevel = LogLevel.VERBOSE, message: String, tag: String? = null, throwable: Throwable? = null)

    /**
     * A fatal [Throwable] has been thrown by the App. This happens when something catastrophic happens, something that cannot be recovered from.
     *
     * @param tag The tag to use in logging.
     * @param throwable The [Throwable] that has been thrown by the fatal event.
     */
    fun onFatalThrowable(tag: String?, throwable: Throwable)
}