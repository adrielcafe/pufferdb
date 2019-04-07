package cafe.adriel.pufferdb.core

import java.io.Serializable

object TestUtil {
    // TODO Try to avoid this explicit delay
    const val CHANNEL_DELAY = 1000L

    const val KEY_DOUBLE = "doubleValue"
    const val KEY_FLOAT = "floatValue"
    const val KEY_INT = "intValue"
    const val KEY_LONG = "longValue"
    const val KEY_BOOLEAN = "booleanValue"
    const val KEY_STRING = "stringValue"
    const val KEY_SERIALIZABLE = "serializableValue"

    const val VALUE_DOUBLE = 12.34
    const val VALUE_FLOAT = 56.78F
    const val VALUE_INT = 123
    const val VALUE_LONG = 456L
    const val VALUE_BOOLEAN = true
    const val VALUE_STRING = "Hello Puffer!"
    val VALUE_SERIALIZABLE = TestSerializable()

    val ALL_SUPPORTED_TYPES = setOf(
        KEY_DOUBLE to VALUE_DOUBLE,
        KEY_FLOAT to VALUE_FLOAT,
        KEY_INT to VALUE_INT,
        KEY_LONG to VALUE_LONG,
        KEY_BOOLEAN to VALUE_BOOLEAN,
        KEY_STRING to VALUE_STRING
    )

    class TestSerializable : Serializable
}
