package cafe.adriel.pufferdb.sample

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import cafe.adriel.pufferdb.android.AndroidPufferDB
import com.ironz.binaryprefs.BinaryPreferencesBuilder
import com.orhanobut.hawk.Hawk
import com.tencent.mmkv.MMKV
import io.paperdb.Paper
import java.io.File
import kotlin.system.measureTimeMillis

class Benchmark(context: Context) {

    companion object {

        private const val REPEAT_COUNT = 1_000
    }

    private val keys = Array(REPEAT_COUNT) { "Key $it" }
    private val values = Array(REPEAT_COUNT) { "Value $it" }

    private val pufferFile = AndroidPufferDB.getInternalFile("puffer.db")
    private val puffer = AndroidPufferDB.withDefault()

    private val sharedPreferencesDir = File(context.filesDir, "preferences")
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    private val mmkvDir = File(MMKV.getRootDir())
    private val mmkv = MMKV.defaultMMKV()

    private val paper = Paper.book()
    private val paperDir = File(paper.path)

    private val binaryPrefsDir = File(context.filesDir, "binaryPrefs")
    private val binaryPrefs = BinaryPreferencesBuilder(context).customDirectory(binaryPrefsDir).build()

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

        binaryPrefsWrite()
        binaryPrefsRead()

        hawkWrite()
        hawkRead()
    }

    private fun reset() {
        puffer.removeAll()
        sharedPreferences.edit(commit = true) { clear() }
        mmkv.clearAll()
        paper.destroy()
        binaryPrefs.edit().clear().commit()
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
                sharedPreferences.edit {
                    putString(keys[i], values[i])
                }
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

    private fun binaryPrefsWrite() {
        val duration = measureTimeMillis {
            repeat(REPEAT_COUNT) { i ->
                binaryPrefs
                    .edit()
                    .putString(keys[i], values[i])
                    .apply()
            }
        }
        Log.i("BINARY PREFS WRITE", "$duration ms")
    }

    private fun binaryPrefsRead() {
        val duration = measureTimeMillis {
            repeat(REPEAT_COUNT) { i ->
                binaryPrefs.getString(keys[i], null)
            }
        }
        Log.i("BINARY PREFS READ", "$duration ms")
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

    private fun File.lengthRecursively(): Long =
        walk().fold(0L) { acc, file ->
            acc + file.length()
        }
}
