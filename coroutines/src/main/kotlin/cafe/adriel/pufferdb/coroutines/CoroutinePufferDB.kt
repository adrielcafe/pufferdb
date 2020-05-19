package cafe.adriel.pufferdb.coroutines

import cafe.adriel.pufferdb.core.PufferDB
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope

class CoroutinePufferDB private constructor(
    pufferFile: File,
    scope: CoroutineScope,
    private val dispatcher: CoroutineContext
) : CoroutinePuffer {

    companion object {

        fun with(pufferFile: File): CoroutinePuffer =
            CoroutinePufferDB(pufferFile, GlobalScope, Dispatchers.IO)

        fun with(
            pufferFile: File,
            scope: CoroutineScope,
            dispatcher: CoroutineContext = Dispatchers.IO
        ): CoroutinePuffer =
            CoroutinePufferDB(pufferFile, scope, dispatcher)
    }

    private val puffer by lazy { PufferDB.with(pufferFile, scope, dispatcher) }

    override suspend fun <T : Any> get(key: String, defaultValue: T?) =
        puffer.getSuspend(key, defaultValue, dispatcher)

    override suspend fun <T : Any> put(key: String, value: T) =
        puffer.putSuspend(key, value, dispatcher)

    override suspend fun getKeys(): Set<String> =
        puffer.getKeysSuspend(dispatcher)

    override suspend fun contains(key: String) =
        puffer.containsSuspend(key, dispatcher)

    override suspend fun remove(key: String) =
        puffer.removeSuspend(key, dispatcher)

    override suspend fun removeAll() =
        puffer.removeAllSuspend(dispatcher)
}
