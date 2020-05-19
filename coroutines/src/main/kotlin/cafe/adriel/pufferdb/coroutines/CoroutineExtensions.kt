package cafe.adriel.pufferdb.coroutines

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

internal suspend fun <T> runSuspend(
    context: CoroutineContext,
    body: suspend CoroutineScope.() -> T
) =
    withContext(context) { body() }

internal fun <T> runAsync(
    scope: CoroutineScope,
    dispatcher: CoroutineContext,
    body: suspend CoroutineScope.() -> T
) =
    scope.async(dispatcher) { body() }
