package cafe.adriel.pufferdb.sample

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import cafe.adriel.pufferdb.android.AndroidPufferDB
import com.orhanobut.hawk.Hawk
import com.tencent.mmkv.MMKV
import io.paperdb.Paper
import kotlin.system.measureTimeMillis

class Benchmark(context: Context) {
    companion object {
        private const val REPEAT_COUNT = 1000
    }

    private val keys = Array(REPEAT_COUNT) { "Key $it" }
    private val values = Array(REPEAT_COUNT) { "Value $it" }

    private val puffer = AndroidPufferDB.withDefault(context)
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val mmkv = MMKV.defaultMMKV()
    private val paper = Paper.book()

    fun run() {
        reset()

        pufferWrite()
        pufferRead()

        sharedPreferencesWrite()
        sharedPreferencesRead()

        mmkvWrite()
        mmkvRead()

        paperWrite()
        paperRead()

        hawkWrite()
        hawkRead()
    }

    @SuppressLint("ApplySharedPref")
    private fun reset() {
        puffer.removeAll()
        sharedPreferences.edit().clear().commit()
        mmkv.clearAll()
        paper.destroy()
        Hawk.destroy()
    }

    private fun pufferWrite() {
        val duration = measureTimeMillis {
            repeat(REPEAT_COUNT) { i ->
                puffer.put(keys[i], values[i])
            }
        }
        Log.i("PUFFER WRITE", "$duration ms")
    }

    private fun pufferRead() {
        val duration = measureTimeMillis {
            repeat(REPEAT_COUNT) { i ->
                puffer.get<String>(keys[i])
            }
        }
        Log.i("PUFFER READ", "$duration ms")
    }

    private fun sharedPreferencesWrite() {
        val duration = measureTimeMillis {
            repeat(REPEAT_COUNT) { i ->
                sharedPreferences
                    .edit()
                    .putString(keys[i], values[i])
                    .apply()
            }
        }
        Log.i("SHARED PREF WRITE", "$duration ms")
    }

    private fun sharedPreferencesRead() {
        val duration = measureTimeMillis {
            repeat(REPEAT_COUNT) { i ->
                sharedPreferences.getString(keys[i], null)
            }
        }
        Log.i("SHARED PREF READ", "$duration ms")
    }

    private fun mmkvWrite() {
        val duration = measureTimeMillis {
            repeat(REPEAT_COUNT) { i ->
                mmkv.putString(keys[i], values[i])
            }
        }
        Log.i("MMKV WRITE", "$duration ms")
    }

    private fun mmkvRead() {
        val duration = measureTimeMillis {
            repeat(REPEAT_COUNT) { i ->
                mmkv.getString(keys[i], null)
            }
        }
        Log.i("MMKV READ", "$duration ms")
    }

    private fun paperWrite() {
        val duration = measureTimeMillis {
            repeat(REPEAT_COUNT) { i ->
                paper.write(keys[i], values[i])
            }
        }
        Log.i("PAPER WRITE", "$duration ms")
    }

    private fun paperRead() {
        val duration = measureTimeMillis {
            repeat(REPEAT_COUNT) { i ->
                paper.read<String>(keys[i])
            }
        }
        Log.i("PAPER READ", "$duration ms")
    }

    private fun hawkWrite() {
        val duration = measureTimeMillis {
            repeat(REPEAT_COUNT) { i ->
                Hawk.put(keys[i], values[i])
            }
        }
        Log.i("HAWK WRITE", "$duration ms")
    }

    private fun hawkRead() {
        val duration = measureTimeMillis {
            repeat(REPEAT_COUNT) { i ->
                Hawk.get<String>(keys[i])
            }
        }
        Log.i("HAWK READ", "$duration ms")
    }
}
