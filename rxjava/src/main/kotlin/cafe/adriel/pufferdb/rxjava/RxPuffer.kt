package cafe.adriel.pufferdb.rxjava

import io.reactivex.Completable
import io.reactivex.Single

interface RxPuffer {

    fun <T : Any> get(key: String, defaultValue: T? = null): Single<T>

    fun <T : Any> put(key: String, value: T): Completable

    fun getKeys(): Single<Set<String>>

    fun contains(key: String): Single<Boolean>

    fun remove(key: String): Completable

    fun removeAll(): Completable
}
