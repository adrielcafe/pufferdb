package cafe.adriel.pufferdb.rxjava

import cafe.adriel.pufferdb.core.PufferDB
import java.io.File

class RxPufferDB private constructor(pufferFile: File) : RxPuffer {

    companion object {
        fun with(pufferFile: File): RxPuffer = RxPufferDB(pufferFile)
    }

    private val puffer by lazy { PufferDB.with(pufferFile) }

    override fun <T : Any> get(key: String, defaultValue: T?) = puffer.getSingle(key, defaultValue)

    override fun <T : Any> put(key: String, value: T) = puffer.putCompletable(key, value)

    override fun getKeys() = puffer.getKeysSingle()

    override fun contains(key: String) = puffer.containsSingle(key)

    override fun remove(key: String) = puffer.removeCompletable(key)

    override fun removeAll() = puffer.removeAllCompletable()
}
