package cafe.adriel.pufferdb.coroutines

import cafe.adriel.pufferdb.core.PufferDB
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope

class CoroutinePufferDB private constructor(
    scope: CoroutineScope,
    context: CoroutineContext,
    pufferFile: File
) : CoroutinePuffer {

    companion object {

        fun with(pufferFile: File): CoroutinePuffer =
            CoroutinePufferDB(GlobalScope, Dispatchers.IO, pufferFile)

        fun with(scope: CoroutineScope, context: CoroutineContext, pufferFile: File): CoroutinePuffer =
            CoroutinePufferDB(scope, context, pufferFile)
    }

    private val puffer by lazy { PufferDB.with(scope, context, pufferFile) }

    override suspend fun <T : Any> get(key: String, defaultValue: T?) = puffer.suspendGet(key, defaultValue)

    override suspend fun <T : Any> put(key: String, value: T) = puffer.suspendPut(key, value)

    override suspend fun getKeys(): Set<String> = puffer.suspendGetKeys()

    override suspend fun contains(key: String) = puffer.suspendContains(key)

    override suspend fun remove(key: String) = puffer.suspendRemove(key)

    override suspend fun removeAll() = puffer.suspendRemoveAll()
}
