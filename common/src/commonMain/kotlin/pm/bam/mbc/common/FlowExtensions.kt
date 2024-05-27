package pm.bam.mbc.common

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart


fun <T : Any> T.toFlow(): Flow<T> = flow { emit(this@toFlow) }

/** Catches and re-throws any [Throwable] that happen upstream after performing [action] on the caught [Throwable]. */
inline fun <T> Flow<T>.onError(crossinline action: suspend FlowCollector<T>.(cause: Throwable) -> Unit): Flow<T> =
    catch {
        action(it)
        emitAll(flow { throw it })
    }

/**
 * Returns a flow that delays the given [delayMillis] **before** this flow starts to be collected.
 */
fun <T> Flow<T>.delayOnStart(delayMillis: Long): Flow<T> = onStart { delay(delayMillis) }