package cafe.adriel.pufferdb.coroutines

import cafe.adriel.pufferdb.core.Puffer
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

// Suspend functions

suspend fun <T : Any> Puffer.suspendGet(key: String, defaultValue: T? = null, context: CoroutineContext? = null) =
    runSuspend(context) { get(key, defaultValue) }

suspend fun <T : Any> Puffer.suspendPut(key: String, value: T, context: CoroutineContext? = null) =
    runSuspend(context) { put(key, value) }

suspend fun Puffer.suspendGetKeys(context: CoroutineContext? = null) =
    runSuspend(context) { getKeys() }

suspend fun Puffer.suspendContains(key: String, context: CoroutineContext? = null) =
    runSuspend(context) { contains(key) }

suspend fun Puffer.suspendRemove(key: String, context: CoroutineContext? = null) =
    runSuspend(context) { remove(key) }

suspend fun Puffer.suspendRemoveAll(context: CoroutineContext? = null) =
    runSuspend(context) { removeAll() }

// Async functions

fun <T : Any> Puffer.getAsync(key: String, defaultValue: T? = null, scope: CoroutineScope? = null) =
    runAsync(scope) { get(key, defaultValue) }

fun <T : Any> Puffer.putAsync(key: String, value: T, scope: CoroutineScope? = null) =
    runAsync(scope) { put(key, value) }

fun Puffer.getKeysAsync(scope: CoroutineScope? = null) =
    runAsync(scope) { getKeys() }

fun Puffer.containsAsync(key: String, scope: CoroutineScope? = null) =
    runAsync(scope) { contains(key) }

fun Puffer.removeAsync(key: String, scope: CoroutineScope? = null) =
    runAsync(scope) { remove(key) }

fun Puffer.removeAllAsync(scope: CoroutineScope? = null) =
    runAsync(scope) { removeAll() }
