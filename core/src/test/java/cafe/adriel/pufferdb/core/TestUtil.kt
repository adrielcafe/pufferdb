package cafe.adriel.pufferdb.core

import java.io.Serializable

object TestUtil {
    // TODO Try to avoid this explicit delay
    const val CHANNEL_DELAY = 3000L

    const val KEY_DOUBLE = "doubleValue"
    const val KEY_DOUBLE_LIST = "doubleListValue"
    const val KEY_FLOAT = "floatValue"
    const val KEY_FLOAT_LIST = "floatListValue"
    const val KEY_INT = "intValue"
    const val KEY_INT_LIST = "intListValue"
    const val KEY_LONG = "longValue"
    const val KEY_LONG_LIST = "longListValue"
    const val KEY_BOOLEAN = "booleanValue"
    const val KEY_BOOLEAN_LIST = "booleanListValue"
    const val KEY_STRING = "stringValue"
    const val KEY_STRING_LIST = "stringListValue"
    const val KEY_NULL_LIST = "nullListValue"
    const val KEY_SERIALIZABLE = "serializableValue"
    const val KEY_SERIALIZABLE_LIST = "serializableListValue"

    const val VALUE_DOUBLE = 123.456
    val VALUE_DOUBLE_LIST = listOf(123.456, 456.789, 789.123)
    const val VALUE_FLOAT = 123.456F
    val VALUE_FLOAT_LIST = listOf(12.34F, 56.78F, 91.23F)
    const val VALUE_INT = 123
    val VALUE_INT_LIST = listOf(12, 34, 56)
    const val VALUE_LONG = 456L
    val VALUE_LONG_LIST = listOf(123L, 456L, 789L)
    const val VALUE_BOOLEAN = true
    val VALUE_BOOLEAN_LIST = listOf(true, false, true)
    const val VALUE_STRING = "Hello Puffer!"
    val VALUE_STRING_LIST = listOf("Hello", "Puffer", "!")
    val VALUE_NULL_LIST = listOf("Hello", "Puffer", null)
    val VALUE_SERIALIZABLE = TestSerializable()
    val VALUE_SERIALIZABLE_LIST = listOf(TestSerializable(), TestSerializable(), TestSerializable())

    val ALL_SUPPORTED_TYPES = setOf(
        KEY_DOUBLE to VALUE_DOUBLE,
        KEY_DOUBLE_LIST to VALUE_DOUBLE_LIST,
        KEY_FLOAT to VALUE_FLOAT,
        KEY_FLOAT_LIST to VALUE_FLOAT_LIST,
        KEY_INT to VALUE_INT,
        KEY_INT_LIST to VALUE_INT_LIST,
        KEY_LONG to VALUE_LONG,
        KEY_LONG_LIST to VALUE_LONG_LIST,
        KEY_BOOLEAN to VALUE_BOOLEAN,
        KEY_BOOLEAN_LIST to VALUE_BOOLEAN_LIST,
        KEY_STRING to VALUE_STRING,
        KEY_STRING_LIST to VALUE_STRING_LIST
    )

    class TestSerializable : Serializable
}
