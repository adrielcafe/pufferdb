package cafe.adriel.pufferdb.coroutines

interface CoroutinePuffer {

    suspend fun <T : Any> get(key: String, defaultValue: T? = null): T

    suspend fun <T : Any> put(key: String, value: T)

    suspend fun getKeys(): Set<String>

    suspend fun contains(key: String): Boolean

    suspend fun remove(key: String)

    suspend fun removeAll()
}
