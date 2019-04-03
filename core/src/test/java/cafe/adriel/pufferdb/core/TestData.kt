package cafe.adriel.pufferdb.core

import java.io.Serializable

object TestData {
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

    val ITEMS = setOf(
        TestData.KEY_LONG to TestData.VALUE_LONG,
        TestData.KEY_BOOLEAN to TestData.VALUE_BOOLEAN,
        TestData.KEY_STRING to TestData.VALUE_STRING
    )

    class TestSerializable : Serializable
}
