package pm.bam.mbc.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transformLatest
import kotlinx.datetime.Clock


/**
 * https://issuetracker.google.com/issues/336842920
 * https://www.reddit.com/r/androiddev/comments/1csjwne/lifecycle_280_only_compatible_with_compose_17
 */
@Composable
fun <T> StateFlow<T>.collectAsStateWithLifecycleFix(): State<T> = this.collectAsStateWithLifecycle(
    lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
)

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


/**
 * Returns a flow containing the results of applying the given [transformLatest] function to each value of the original flow
 * only after given [delayMillis] has passed, either because the transformation took more or equal amount of time as the [delayMillis],
 * or because the suspend function was delayed for the remaining time.
 */
@OptIn(ExperimentalCoroutinesApi::class)
inline fun <T, R> Flow<T>.flatMapLatestDelayAtLeast(delayMillis: Long, crossinline transformFunction: suspend (value: T) -> R): Flow<R> =
    transformLatest { value ->

        val timeBeforeTransformation = Clock.System.now().toEpochMilliseconds()
        val transformedTransformation = transformFunction(value)
        val timeAfterTransformation = Clock.System.now().toEpochMilliseconds()

        val transformationDuration = timeAfterTransformation - timeBeforeTransformation

        val transformationShorterThanDelay = delayMillis > transformationDuration

        if (transformationShorterThanDelay) {
            val remainingDelay = delayMillis - transformationDuration
            delay(remainingDelay)
        }

        return@transformLatest emit(transformedTransformation)
    }