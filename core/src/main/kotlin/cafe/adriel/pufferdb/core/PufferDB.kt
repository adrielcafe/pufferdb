package cafe.adriel.pufferdb.core

import cafe.adriel.pufferdb.proto.PufferProto
import cafe.adriel.pufferdb.proto.ValueProto
import cafe.adriel.pufferdb.proto.ValueProto.TypeCase.BOOL_VALUE
import cafe.adriel.pufferdb.proto.ValueProto.TypeCase.DOUBLE_VALUE
import cafe.adriel.pufferdb.proto.ValueProto.TypeCase.FLOAT_VALUE
import cafe.adriel.pufferdb.proto.ValueProto.TypeCase.INT_VALUE
import cafe.adriel.pufferdb.proto.ValueProto.TypeCase.LONG_VALUE
import cafe.adriel.pufferdb.proto.ValueProto.TypeCase.STRING_VALUE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.File
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

class PufferDB private constructor(private val pufferFile: File) : Puffer {

    companion object {
        fun with(pufferFile: File): Puffer = PufferDB(pufferFile)
    }

    private val nest = ConcurrentHashMap<String, Any>()

    init {
        loadProto()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(key: String, defaultValue: T?) = try {
        val value = nest.getOrDefault(key, null)
        value as? T ?: throw PufferException("The key '$key' has no value saved")
    } catch (e: PufferException) {
        defaultValue ?: throw e
    }

    override fun <T : Any> put(key: String, value: T) {
        if (isTypeSupported(value)) {
            nest[key] = value
            saveProto()
        } else {
            throw PufferException("${value::class.java.name} is not supported")
        }
    }

    override fun getKeys() = nest.keys().toList().toSet()

    override fun contains(key: String) = nest.containsKey(key)

    override fun remove(key: String) {
        nest.remove(key)
        saveProto()
    }

    override fun removeAll() {
        nest.clear()
        saveProto()
    }

    private fun loadProto() = try {
        val currentNest = if (pufferFile.exists()) {
            PufferProto
                .parseFrom(pufferFile.inputStream())
                .nestMap
                .mapValues { getValue(it.value) }
        } else {
            pufferFile.createNewFile()
            emptyMap()
        }
        nest.apply {
            clear()
            putAll(currentNest)
        }
    } catch (e: IOException) {
        throw PufferException("Unable to read ${pufferFile.path}", e)
    }

    private fun saveProto() = GlobalScope.async(Dispatchers.IO) {
        try {
            val newNest = nest.mapValues {
                getProtoValue(it.value)
            }
            PufferProto.newBuilder()
                .putAllNest(newNest)
                .build()
                .writeTo(pufferFile.outputStream())
        } catch (e: IOException) {
            throw PufferException("Unable to write in ${pufferFile.path}", e)
        }
    }.start()

    private fun isTypeSupported(value: Any?) = when (value) {
        is Double, is Float, is Int, is Long, is Boolean, is String -> true
        else -> false
    }

    private fun getValue(value: ValueProto?): Any = when (value?.typeCase) {
        DOUBLE_VALUE -> value.doubleValue
        FLOAT_VALUE -> value.floatValue
        INT_VALUE -> value.intValue
        LONG_VALUE -> value.longValue
        BOOL_VALUE -> value.boolValue
        STRING_VALUE -> value.stringValue
        else -> if (value == null) {
            throw PufferException("Value cannot be null")
        } else {
            throw PufferException("No value found")
        }
    }

    private fun getProtoValue(value: Any?) = ValueProto.newBuilder()
        .also { builder ->
            when (value) {
                is Double -> builder.doubleValue = value
                is Float -> builder.floatValue = value
                is Int -> builder.intValue = value
                is Long -> builder.longValue = value
                is Boolean -> builder.boolValue = value
                is String -> builder.stringValue = value
                else -> if (value == null) {
                    throw PufferException("Value cannot be null")
                } else {
                    throw PufferException("${value::class.java.name} is not supported")
                }
            }
        }
        .build()
}
