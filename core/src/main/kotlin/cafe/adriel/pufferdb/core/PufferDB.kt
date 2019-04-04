package cafe.adriel.pufferdb.core

import cafe.adriel.pufferdb.proto.PufferProto
import cafe.adriel.pufferdb.proto.ValueProto
import cafe.adriel.pufferdb.proto.ValueProto.TypeCase.BOOL_VALUE
import cafe.adriel.pufferdb.proto.ValueProto.TypeCase.DOUBLE_VALUE
import cafe.adriel.pufferdb.proto.ValueProto.TypeCase.FLOAT_VALUE
import cafe.adriel.pufferdb.proto.ValueProto.TypeCase.INT_VALUE
import cafe.adriel.pufferdb.proto.ValueProto.TypeCase.LONG_VALUE
import cafe.adriel.pufferdb.proto.ValueProto.TypeCase.STRING_VALUE
import java.io.File
import java.io.IOException
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class PufferDB private constructor(private val pufferFile: File) : Puffer {

    companion object {
        fun with(pufferFile: File): Puffer = PufferDB(pufferFile)
    }

    private val locker = ReentrantReadWriteLock()
    private var proto = loadProto()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(key: String, defaultValue: T?) = try {
        val protoValue = locker.read {
            proto.nestMap.getOrElse(key) { null }
        }

        val value = getValueFromProto(protoValue) as? T
        value ?: throw PufferException("The key '$key' has no value saved")
    } catch (e: PufferException) {
        defaultValue ?: throw e
    }

    override fun <T : Any> put(key: String, value: T) {
        val protoValue = ValueProto.newBuilder()
            .also { builder ->
                when (value) {
                    is Double -> builder.doubleValue = value
                    is Float -> builder.floatValue = value
                    is Int -> builder.intValue = value
                    is Long -> builder.longValue = value
                    is Boolean -> builder.boolValue = value
                    is String -> builder.stringValue = value
                    else -> throw PufferException("${value::class.java.name} is not supported")
                }
            }
            .build()

        write {
            putNest(key, protoValue)
        }
    }

    override fun getKeys() = locker.read {
        proto.nestMap.keys.toSet()
    }

    override fun contains(key: String) = locker.read {
        proto.containsNest(key)
    }

    override fun remove(key: String) = write {
        removeNest(key)
    }

    override fun removeAll() = locker.write {
        proto = PufferProto.getDefaultInstance()
        saveProto()
    }

    private fun write(action: PufferProto.Builder.() -> Unit) = locker.write {
        proto = PufferProto.newBuilder(proto).run {
            action(this)
            build()
        }
        saveProto()
    }

    private fun loadProto() = locker.write {
        try {
            PufferProto.parseFrom(pufferFile.inputStream())
        } catch (e: IOException) {
            throw PufferException("Unable to read ${pufferFile.path}", e)
        }
    }

    private fun saveProto() = try {
        proto.writeTo(pufferFile.outputStream())
    } catch (e: IOException) {
        throw PufferException("Unable to write in ${pufferFile.path}", e)
    }

    private fun getValueFromProto(value: ValueProto?): Any? = when (value?.typeCase) {
        DOUBLE_VALUE -> value.doubleValue
        FLOAT_VALUE -> value.floatValue
        INT_VALUE -> value.intValue
        LONG_VALUE -> value.longValue
        BOOL_VALUE -> value.boolValue
        STRING_VALUE -> value.stringValue
        else -> null
    }
}
