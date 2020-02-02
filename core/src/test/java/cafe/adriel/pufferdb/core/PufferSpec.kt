package cafe.adriel.pufferdb.core

import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.setMain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isTrue

object PufferSpec : Spek({
    val scope = TestCoroutineScope()
    val context = TestCoroutineDispatcher()

    val pufferFile by memoized {
        spyk(File.createTempFile("puffer", ".db"))
    }
    val puffer by memoized {
        spyk(PufferDB.with(scope, context, pufferFile))
    }

    beforeEachTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    afterEachTest {
        pufferFile.delete()
    }

    describe("reading/writing files") {
        it("should load a valid file") {
            val allKeys = mutableSetOf<String>()
            TestUtil.ALL_SUPPORTED_TYPES.forEach {
                allKeys.add(it.first)
                puffer.put(it.first, it.second)
            }

            val newPuffer = PufferDB.with(scope, context, pufferFile)

            expectThat(newPuffer.getKeys()).isEqualTo(allKeys)
        }

        it("should create a new file if it does not exists") {
            pufferFile.delete()

            PufferDB.with(scope, context, pufferFile)

            expectThat(pufferFile.exists()).isTrue()
        }

        it("should check for write permission") {
            every { pufferFile.canWrite() } returns false

            puffer.put(TestUtil.KEY_STRING, TestUtil.VALUE_STRING)

            coVerify { pufferFile.canWrite() }
        }

        it("should save when put an item") {
            pufferFile.delete()

            puffer.put(TestUtil.KEY_STRING, TestUtil.VALUE_STRING)

            expectThat(pufferFile.exists()).isTrue()
        }

        it("should save when remove an item") {
            pufferFile.delete()

            puffer.put(TestUtil.KEY_STRING, TestUtil.VALUE_STRING)
            puffer.remove(TestUtil.KEY_STRING)

            expectThat(pufferFile.exists()).isTrue()
        }

        it("should save when remove all items") {
            pufferFile.delete()

            puffer.put(TestUtil.KEY_STRING, TestUtil.VALUE_STRING)
            puffer.removeAll()

            expectThat(pufferFile.exists()).isTrue()
        }
    }

    describe("saving single items") {
        it("should save a double value") {
            puffer.put(TestUtil.KEY_DOUBLE, TestUtil.VALUE_DOUBLE)

            val value = puffer.get<Double>(TestUtil.KEY_DOUBLE)
            expectThat(value).isEqualTo(TestUtil.VALUE_DOUBLE)
        }

        it("should save a float value") {
            puffer.put(TestUtil.KEY_FLOAT, TestUtil.VALUE_FLOAT)

            val value = puffer.get<Float>(TestUtil.KEY_FLOAT)
            expectThat(value).isEqualTo(TestUtil.VALUE_FLOAT)
        }

        it("should save a int value") {
            puffer.put(TestUtil.KEY_INT, TestUtil.VALUE_INT)

            val value = puffer.get<Int>(TestUtil.KEY_INT)
            expectThat(value).isEqualTo(TestUtil.VALUE_INT)
        }

        it("should save a long value") {
            puffer.put(TestUtil.KEY_LONG, TestUtil.VALUE_LONG)

            val value = puffer.get<Long>(TestUtil.KEY_LONG)
            expectThat(value).isEqualTo(TestUtil.VALUE_LONG)
        }

        it("should save a boolean value") {
            puffer.put(TestUtil.KEY_BOOLEAN, TestUtil.VALUE_BOOLEAN)

            val value = puffer.get<Boolean>(TestUtil.KEY_BOOLEAN)
            expectThat(value).isEqualTo(TestUtil.VALUE_BOOLEAN)
        }

        it("should save a string value") {
            puffer.put(TestUtil.KEY_STRING, TestUtil.VALUE_STRING)

            val value = puffer.get<String>(TestUtil.KEY_STRING)
            expectThat(value).isEqualTo(TestUtil.VALUE_STRING)
        }

        it("should throw when type is not supported") {
            expectThrows<PufferException> {
                puffer.put(TestUtil.KEY_SERIALIZABLE, TestUtil.VALUE_SERIALIZABLE)
            }
        }
    }

    describe("saving list items") {
        it("should save a double list") {
            puffer.put(TestUtil.KEY_DOUBLE_LIST, TestUtil.VALUE_DOUBLE_LIST)

            val value = puffer.get<List<Double>>(TestUtil.KEY_DOUBLE_LIST)
            expectThat(value).isEqualTo(TestUtil.VALUE_DOUBLE_LIST)
        }

        it("should save a float list") {
            puffer.put(TestUtil.KEY_FLOAT_LIST, TestUtil.VALUE_FLOAT_LIST)

            val value = puffer.get<List<Float>>(TestUtil.KEY_FLOAT_LIST)
            expectThat(value).isEqualTo(TestUtil.VALUE_FLOAT_LIST)
        }

        it("should save a int list") {
            puffer.put(TestUtil.KEY_INT_LIST, TestUtil.VALUE_INT_LIST)

            val value = puffer.get<List<Int>>(TestUtil.KEY_INT_LIST)
            expectThat(value).isEqualTo(TestUtil.VALUE_INT_LIST)
        }

        it("should save a long list") {
            puffer.put(TestUtil.KEY_LONG_LIST, TestUtil.VALUE_LONG_LIST)

            val value = puffer.get<List<Long>>(TestUtil.KEY_LONG_LIST)
            expectThat(value).isEqualTo(TestUtil.VALUE_LONG_LIST)
        }

        it("should save a boolean list") {
            puffer.put(TestUtil.KEY_BOOLEAN_LIST, TestUtil.VALUE_BOOLEAN_LIST)

            val value = puffer.get<List<Boolean>>(TestUtil.KEY_BOOLEAN_LIST)
            expectThat(value).isEqualTo(TestUtil.VALUE_BOOLEAN_LIST)
        }

        it("should save a string list") {
            puffer.put(TestUtil.KEY_STRING_LIST, TestUtil.VALUE_STRING_LIST)

            val value = puffer.get<List<String>>(TestUtil.KEY_STRING_LIST)
            expectThat(value).isEqualTo(TestUtil.VALUE_STRING_LIST)
        }

        it("should throw when type is not supported") {
            expectThrows<PufferException> {
                puffer.put(TestUtil.KEY_SERIALIZABLE_LIST, TestUtil.VALUE_SERIALIZABLE_LIST)
            }
        }

        it("should throw when list has null value") {
            expectThrows<PufferException> {
                puffer.put(TestUtil.KEY_NULL_LIST, TestUtil.VALUE_NULL_LIST)
            }
        }
    }

    describe("retrieving items") {
        it("should return the default value") {
            val value = puffer.get(TestUtil.KEY_STRING, TestUtil.VALUE_STRING)
            expectThat(value).isEqualTo(TestUtil.VALUE_STRING)
        }

        it("should throw when value do not exists") {
            expectThrows<PufferException> {
                puffer.get(TestUtil.KEY_STRING)
            }
        }

        it("should throw when try to get a removed value") {
            puffer.put(TestUtil.KEY_STRING, TestUtil.VALUE_STRING)

            puffer.remove(TestUtil.KEY_STRING)

            expectThrows<PufferException> {
                puffer.get(TestUtil.KEY_STRING)
            }
        }
    }

    describe("retrieving all keys") {
        it("should return empty") {
            val keys = puffer.getKeys()
            expectThat(keys).isEmpty()
        }

        it("should return all keys") {
            val allKeys = mutableSetOf<String>()
            TestUtil.ALL_SUPPORTED_TYPES.forEach {
                allKeys.add(it.first)
                puffer.put(it.first, it.second)
            }

            val keys = puffer.getKeys()
            expectThat(keys).isEqualTo(allKeys)
        }
    }

    describe("checking if values exists") {
        it("should return true if exists") {
            puffer.put(TestUtil.KEY_STRING, TestUtil.VALUE_STRING)

            val contains = puffer.contains(TestUtil.KEY_STRING)
            expectThat(contains).isTrue()
        }

        it("should return false if not found") {
            val contains = puffer.contains(TestUtil.KEY_STRING)
            expectThat(contains).isFalse()
        }
    }

    describe("removing items") {
        it("should remove the key and value") {
            puffer.put(TestUtil.KEY_STRING, TestUtil.VALUE_STRING)

            puffer.remove(TestUtil.KEY_STRING)

            val contains = puffer.contains(TestUtil.KEY_STRING)
            expectThat(contains).isFalse()
        }

        it("should remove all items") {
            TestUtil.ALL_SUPPORTED_TYPES.forEach {
                puffer.put(it.first, it.second)
            }

            puffer.removeAll()

            val keys = puffer.getKeys()
            expectThat(keys).isEmpty()
        }
    }
})
