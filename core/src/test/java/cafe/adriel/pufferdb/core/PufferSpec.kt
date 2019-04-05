package cafe.adriel.pufferdb.core

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isTrue
import java.io.File

object PufferSpec : Spek({
    val pufferFile by memoized { File.createTempFile("puffer", ".db") }
    val puffer by memoized { PufferDB.with(pufferFile) }

    describe("reading/writing files") {
        it("should throw when read an invalid file") {
            val invalidPufferFile = File("\"/.db\"")

            expectThrows<PufferException> {
                PufferDB.with(invalidPufferFile)
            }
        }

        it("should load a valid file") {
            val allKeys = mutableSetOf<String>()
            runBlocking {
                TestData.ITEMS.forEach {
                    allKeys.add(it.first)
                    puffer.put(it.first, it.second)
                }
                // TODO Try to avoid this explicit delay
                delay(100)
            }

            val newPuffer = PufferDB.with(pufferFile)

            expectThat(newPuffer.getKeys()).isEqualTo(allKeys)
        }

        it("should create a new file if it does not exists") {
            pufferFile.delete()

            PufferDB.with(pufferFile)

            expectThat(pufferFile.exists()).isTrue()
        }

        // TODO Add more tests
    }

    describe("saving items") {
        it("should save a double value") {
            puffer.put(TestData.KEY_DOUBLE, TestData.VALUE_DOUBLE)

            val value = puffer.get<Double>(TestData.KEY_DOUBLE)
            expectThat(value).isEqualTo(TestData.VALUE_DOUBLE)
        }

        it("should save a float value") {
            puffer.put(TestData.KEY_FLOAT, TestData.VALUE_FLOAT)

            val value = puffer.get<Float>(TestData.KEY_FLOAT)
            expectThat(value).isEqualTo(TestData.VALUE_FLOAT)
        }

        it("should save a int value") {
            puffer.put(TestData.KEY_INT, TestData.VALUE_INT)

            val value = puffer.get<Int>(TestData.KEY_INT)
            expectThat(value).isEqualTo(TestData.VALUE_INT)
        }

        it("should save a long value") {
            puffer.put(TestData.KEY_LONG, TestData.VALUE_LONG)

            val value = puffer.get<Long>(TestData.KEY_LONG)
            expectThat(value).isEqualTo(TestData.VALUE_LONG)
        }

        it("should save a boolean value") {
            puffer.put(TestData.KEY_BOOLEAN, TestData.VALUE_BOOLEAN)

            val value = puffer.get<Boolean>(TestData.KEY_BOOLEAN)
            expectThat(value).isEqualTo(TestData.VALUE_BOOLEAN)
        }

        it("should save a string value") {
            puffer.put(TestData.KEY_STRING, TestData.VALUE_STRING)

            val value = puffer.get<String>(TestData.KEY_STRING)
            expectThat(value).isEqualTo(TestData.VALUE_STRING)
        }

        it("should throw when type is not supported") {
            expectThrows<PufferException> {
                puffer.put(TestData.KEY_SERIALIZABLE, TestData.VALUE_SERIALIZABLE)
            }
        }
    }

    describe("retrieving items") {
        it("should return the default value") {
            val value = puffer.get(TestData.KEY_STRING, TestData.VALUE_STRING)
            expectThat(value).isEqualTo(TestData.VALUE_STRING)
        }

        it("should throw when value do not exists") {
            expectThrows<PufferException> {
                puffer.get(TestData.KEY_STRING)
            }
        }

        it("should throw when try to get a removed value") {
            puffer.put(TestData.KEY_STRING, TestData.VALUE_STRING)

            puffer.remove(TestData.KEY_STRING)

            expectThrows<PufferException> {
                puffer.get(TestData.KEY_STRING)
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
            TestData.ITEMS.forEach {
                allKeys.add(it.first)
                puffer.put(it.first, it.second)
            }

            val keys = puffer.getKeys()
            expectThat(keys).isEqualTo(allKeys)
        }
    }

    describe("checking if values exists") {
        it("should return true if exists") {
            puffer.put(TestData.KEY_STRING, TestData.VALUE_STRING)

            val contains = puffer.contains(TestData.KEY_STRING)
            expectThat(contains).isTrue()
        }

        it("should return false if not found") {
            val contains = puffer.contains(TestData.KEY_STRING)
            expectThat(contains).isFalse()
        }
    }

    describe("removing items") {
        it("should remove the key and value") {
            puffer.put(TestData.KEY_STRING, TestData.VALUE_STRING)

            puffer.remove(TestData.KEY_STRING)

            val contains = puffer.contains(TestData.KEY_STRING)
            expectThat(contains).isFalse()
        }

        it("should remove all items") {
            TestData.ITEMS.forEach {
                puffer.put(it.first, it.second)
            }

            puffer.removeAll()

            val keys = puffer.getKeys()
            expectThat(keys).isEmpty()
        }
    }
})
