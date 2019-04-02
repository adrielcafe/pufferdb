package cafe.adriel.pufferdb.core

interface Puffer {

    fun <T : Any> get(key: String, defaultValue: T? = null): T

    fun <T : Any> put(key: String, value: T)

    fun getKeys(): Set<String>

    fun contains(key: String): Boolean

    fun remove(key: String)

    fun removeAll()
}
