package cafe.adriel.pufferdb.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal suspend fun <T> runSuspend(context: CoroutineContext? = null, body: suspend CoroutineScope.() -> T) =
    withContext(context ?: Dispatchers.IO) { body() }

internal fun <T> runAsync(scope: CoroutineScope? = null, body: suspend CoroutineScope.() -> T) =
    (scope ?: GlobalScope).async { body() }
