package cafe.adriel.pufferdb.coroutines

import cafe.adriel.pufferdb.core.Puffer
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope

// Suspend functions
suspend fun <T : Any> Puffer.getSuspend(
    key: String,
    defaultValue: T? = null,
    dispatcher: CoroutineContext = Dispatchers.IO
) =
    runSuspend(dispatcher) { get(key, defaultValue) }

suspend fun <T : Any> Puffer.putSuspend(key: String, value: T, dispatcher: CoroutineContext = Dispatchers.IO) =
    runSuspend(dispatcher) { put(key, value) }

suspend fun Puffer.getKeysSuspend(dispatcher: CoroutineContext = Dispatchers.IO) =
    runSuspend(dispatcher) { getKeys() }

suspend fun Puffer.containsSuspend(key: String, dispatcher: CoroutineContext = Dispatchers.IO) =
    runSuspend(dispatcher) { contains(key) }

suspend fun Puffer.removeSuspend(key: String, dispatcher: CoroutineContext = Dispatchers.IO) =
    runSuspend(dispatcher) { remove(key) }

suspend fun Puffer.removeAllSuspend(dispatcher: CoroutineContext = Dispatchers.IO) =
    runSuspend(dispatcher) { removeAll() }

// Async functions
fun <T : Any> Puffer.getAsync(
    key: String,
    defaultValue: T? = null,
    scope: CoroutineScope = GlobalScope,
    dispatcher: CoroutineContext = Dispatchers.IO
) =
    runAsync(scope, dispatcher) { get(key, defaultValue) }

fun <T : Any> Puffer.putAsync(
    key: String,
    value: T,
    scope: CoroutineScope = GlobalScope,
    dispatcher: CoroutineContext = Dispatchers.IO
) =
    runAsync(scope, dispatcher) { put(key, value) }

fun Puffer.getKeysAsync(
    scope: CoroutineScope = GlobalScope,
    dispatcher: CoroutineContext = Dispatchers.IO
) =
    runAsync(scope, dispatcher) { getKeys() }

fun Puffer.containsAsync(
    key: String,
    scope: CoroutineScope = GlobalScope,
    dispatcher: CoroutineContext = Dispatchers.IO
) =
    runAsync(scope, dispatcher) { contains(key) }

fun Puffer.removeAsync(
    key: String,
    scope: CoroutineScope = GlobalScope,
    dispatcher: CoroutineContext = Dispatchers.IO
) =
    runAsync(scope, dispatcher) { remove(key) }

fun Puffer.removeAllAsync(
    scope: CoroutineScope = GlobalScope,
    dispatcher: CoroutineContext = Dispatchers.IO
) =
    runAsync(scope, dispatcher) { removeAll() }
