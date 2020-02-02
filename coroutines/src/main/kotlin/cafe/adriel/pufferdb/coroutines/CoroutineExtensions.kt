package cafe.adriel.pufferdb.coroutines

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

internal suspend fun <T> runSuspend(context: CoroutineContext? = null, body: suspend CoroutineScope.() -> T) =
    withContext(context ?: Dispatchers.IO) { body() }

internal fun <T> runAsync(scope: CoroutineScope? = null, body: suspend CoroutineScope.() -> T) =
    (scope ?: GlobalScope).async { body() }
