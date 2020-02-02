package cafe.adriel.pufferdb.core

import cafe.adriel.pufferdb.proto.ValueProto
import cafe.adriel.pufferdb.proto.ValueProto.TypeProto.TypeCase.BOOL_VALUE
import cafe.adriel.pufferdb.proto.ValueProto.TypeProto.TypeCase.DOUBLE_VALUE
import cafe.adriel.pufferdb.proto.ValueProto.TypeProto.TypeCase.FLOAT_VALUE
import cafe.adriel.pufferdb.proto.ValueProto.TypeProto.TypeCase.INT_VALUE
import cafe.adriel.pufferdb.proto.ValueProto.TypeProto.TypeCase.LONG_VALUE
import cafe.adriel.pufferdb.proto.ValueProto.TypeProto.TypeCase.STRING_VALUE

internal fun Any.className(): String = this::class.java.name

internal fun Any.isTypeSupported(): Boolean =
    if (this is List<*>) {
        isListTypeSupported()
    } else {
        isSingleTypeSupported()
    }

private fun List<*>.isListTypeSupported(): Boolean {
    forEach { singleValue ->
        if (singleValue == null || !singleValue.isSingleTypeSupported()) {
            return false
        }
    }
    return true
}

private fun Any.isSingleTypeSupported(): Boolean =
    when (this) {
        is Double, is Float, is Int, is Long, is Boolean, is String -> true
        else -> false
    }

internal fun ValueProto.TypeProto.getSingleValue(): Any? =
    when (typeCase) {
        DOUBLE_VALUE -> doubleValue
        FLOAT_VALUE -> floatValue
        INT_VALUE -> intValue
        LONG_VALUE -> longValue
        BOOL_VALUE -> boolValue
        STRING_VALUE -> stringValue
        else -> null
    }

internal fun List<ValueProto.TypeProto>.getListValue(): List<Any> =
    mapNotNull { it.getSingleValue() }

internal fun Any.getProtoValue(): ValueProto =
    if (this is List<*>) {
        ValueProto.newBuilder().also { builder ->
            this.forEach { item ->
                item?.let { builder.addListValue(getProtoType(it)) }
            }
        }.build()
    } else {
        ValueProto.newBuilder()
            .setSingleValue(getProtoType(this))
            .build()
    }

private fun getProtoType(value: Any): ValueProto.TypeProto =
    ValueProto.TypeProto.newBuilder().also { builder ->
        when (value) {
            is Double -> builder.doubleValue = value
            is Float -> builder.floatValue = value
            is Int -> builder.intValue = value
            is Long -> builder.longValue = value
            is Boolean -> builder.boolValue = value
            is String -> builder.stringValue = value
        }
    }.build()
