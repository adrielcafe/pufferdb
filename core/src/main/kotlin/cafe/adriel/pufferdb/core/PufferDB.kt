package cafe.adriel.pufferdb.core

import cafe.adriel.pufferdb.proto.PufferProto
import cafe.adriel.pufferdb.proto.ValueProto
import java.io.File
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class PufferDB private constructor(
    private val pufferFile: File,
    scope: CoroutineScope,
    private val dispatcher: CoroutineContext
) : Puffer {

    companion object {

        fun with(pufferFile: File): Puffer =
            PufferDB(pufferFile, GlobalScope, Dispatchers.IO)

        fun with(pufferFile: File, scope: CoroutineScope, dispatcher: CoroutineContext = Dispatchers.IO): Puffer =
            PufferDB(pufferFile, scope, dispatcher)
    }

    private val state = ConcurrentHashMap<String, Any>()
    private val stateFlow = MutableStateFlow(0L)

    private val writeMutex = Mutex()
    private var writeJob: Job? = null

    init {
        loadProto()

        stateFlow
            .drop(1)
            .onEach { saveProto(state) }
            .flowOn(dispatcher)
            .launchIn(scope)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(key: String, defaultValue: T?): T = try {
        val value = state[key]
        if (value == null) {
            throw PufferException("The key '$key' has no value saved")
        } else {
            value as? T
                ?: throw PufferException("Unable to convert '${value.className()}' to the specified typed")
        }
    } catch (e: PufferException) {
        defaultValue ?: throw e
    }

    override fun <T : Any> put(key: String, value: T) {
        if (value.isTypeSupported()) {
            state[key] = value
            updateState()
        } else {
            throw PufferException("${value.className()} is not supported")
        }
    }

    override fun getKeys(): Set<String> =
        state.keys

    override fun contains(key: String) =
        state.containsKey(key)

    override fun remove(key: String) {
        state -= key
        updateState()
    }

    override fun removeAll() {
        state.clear()
        updateState()
    }

    private fun updateState() {
        stateFlow.value = System.currentTimeMillis()
    }

    private fun loadProto() {
        val currentNest = try {
            if (pufferFile.exists()) {
                loadProtoFile()
            } else {
                pufferFile.createNewFile()
                emptyMap()
            }
        } catch (e: IOException) {
            throw PufferException("Unable to read ${pufferFile.path}", e)
        }
        state.run {
            clear()
            putAll(currentNest)
        }
    }

    private fun loadProtoFile(): Map<String, Any> {
        val stream = pufferFile.inputStream()
        val proto = PufferProto
            .parseFrom(stream)
            .nestMap
            .mapNotNull { mapEntry ->
                val value = if (mapEntry.value.hasSingleValue()) {
                    mapEntry.value.singleValue.getSingleValue()
                } else {
                    mapEntry.value.listValueList.getListValue()
                }
                if (value == null) null else mapEntry.key to value
            }
            .toMap()
        stream.close()
        return proto
    }

    private suspend fun saveProto(state: Map<String, Any>) = coroutineScope {
        writeJob?.cancel()
        writeJob = async(dispatcher) {
            writeMutex.withLock {
                if (isActive) {
                    val newNest = state.mapValues { it.value.getProtoValue() }
                    saveProtoFile(newNest)
                }
            }
        }
    }

    private fun saveProtoFile(newNest: Map<String, ValueProto>) {
        try {
            if (!pufferFile.canWrite()) {
                throw IOException("Missing write permission")
            }
            val stream = pufferFile.outputStream()
            PufferProto.newBuilder()
                .putAllNest(newNest)
                .build()
                .writeTo(stream)
            stream.close()
        } catch (e: IOException) {
            throw PufferException("Unable to write in ${pufferFile.path}", e)
        }
    }
}
