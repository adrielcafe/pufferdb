package cafe.adriel.pufferdb.core

import cafe.adriel.pufferdb.proto.PufferProto
import cafe.adriel.pufferdb.proto.ValueProto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

class PufferDB private constructor(private val pufferFile: File) : Puffer {

    companion object {
        fun with(pufferFile: File): Puffer = PufferDB(pufferFile)
    }

    private val nest = ConcurrentHashMap<String, Any>()

    private val writeChannel = Channel<Unit>(Channel.CONFLATED)
    private val writeMutex = Mutex()
    private var writeJob: Job? = null

    init {
        GlobalScope.launch(Dispatchers.Main) {
            /**
             * TODO Replace by Flow when became stable
             * https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/
             */
            writeChannel.consumeEach { saveProto() }
        }

        loadProto()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(key: String, defaultValue: T?): T = try {
        val value = nest.getOrDefault(key, null)
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
            nest[key] = value
            writeChannel.offer(Unit)
        } else {
            throw PufferException("${value.className()} is not supported")
        }
    }

    override fun getKeys(): Set<String> =
        nest.keys().toList().toSet()

    override fun contains(key: String) =
        nest.containsKey(key)

    override fun remove(key: String) {
        nest.remove(key)
        writeChannel.offer(Unit)
    }

    override fun removeAll() {
        nest.clear()
        writeChannel.offer(Unit)
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
        nest.run {
            clear()
            putAll(currentNest)
        }
    }

    private fun loadProtoFile(): Map<String, Any> =
        PufferProto
            .parseFrom(pufferFile.inputStream())
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

    private suspend fun saveProto() =
        coroutineScope {
            writeJob?.cancel()
            writeJob = async {
                writeMutex.withLock {
                    if (isActive) {
                        val newNest = nest.mapValues { it.value.getProtoValue() }
                        saveProtoFile(newNest)
                    }
                }
            }
        }

    private suspend fun saveProtoFile(newNest: Map<String, ValueProto>) =
        withContext(Dispatchers.IO) {
            try {
                if (!pufferFile.canWrite()) {
                    throw IOException("Missing write permission")
                }
                PufferProto.newBuilder()
                    .putAllNest(newNest)
                    .build()
                    .writeTo(pufferFile.outputStream())
            } catch (e: IOException) {
                throw PufferException("Unable to write in ${pufferFile.path}", e)
            }
        }
}
