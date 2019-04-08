package cafe.adriel.pufferdb.sample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import cafe.adriel.pufferdb.android.AndroidPufferDB
import cafe.adriel.pufferdb.coroutines.CoroutinePufferDB
import cafe.adriel.pufferdb.rxjava.RxPufferDB
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity : BaseActivity(), View.OnClickListener {

    companion object {
        private const val TAG_CORE = "PUFFER CORE"
        private const val TAG_COROUTINES = "PUFFER COROUTINES"
        private const val TAG_RX = "PUFFER RX"

        private const val KEY_DOUBLE = "doubleValue"
        private const val KEY_DOUBLE_LIST = "doubleListValue"
        private const val KEY_FLOAT = "floatValue"
        private const val KEY_FLOAT_LIST = "floatListValue"
        private const val KEY_INT = "intValue"
        private const val KEY_INT_LIST = "intListValue"
        private const val KEY_LONG = "longValue"
        private const val KEY_LONG_LIST = "longListValue"
        private const val KEY_BOOLEAN = "booleanValue"
        private const val KEY_BOOLEAN_LIST = "booleanListValue"
        private const val KEY_STRING = "stringValue"
        private const val KEY_STRING_LIST = "stringListValue"

        private const val VALUE_DOUBLE = 123.456
        private val VALUE_DOUBLE_LIST = listOf(123.456, 456.789, 789.123)
        private const val VALUE_FLOAT = 123.456F
        private val VALUE_FLOAT_LIST = listOf(12.34F, 56.78F, 91.23F)
        private const val VALUE_INT = 123
        private val VALUE_INT_LIST = listOf(12, 34, 56)
        private const val VALUE_LONG = 456L
        private val VALUE_LONG_LIST = listOf(123L, 456L, 789L)
        private const val VALUE_BOOLEAN = true
        private val VALUE_BOOLEAN_LIST = listOf(true, false, true)
        private const val VALUE_STRING = "Hello Puffer!"
        private val VALUE_STRING_LIST = listOf("Hello", "Puffer", "!")
    }

    private val pufferCore by lazy {
        AndroidPufferDB.withDefault(this)
    }
    private val pufferCoroutine by lazy {
        val pufferFile = AndroidPufferDB.getInternalFile(this, "puffer_coroutines.db")
        CoroutinePufferDB.with(pufferFile)
    }
    private val pufferRx by lazy {
        val pufferFile = AndroidPufferDB.getInternalFile(this, "puffer_rx.db")
        RxPufferDB.with(pufferFile)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vRunCore.setOnClickListener(this)
        vRunCoroutine.setOnClickListener(this)
        vRunRx.setOnClickListener(this)
        vRunBenchmark.setOnClickListener(this)

        vLogo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.floating))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.vRunCore -> runCore()
            R.id.vRunCoroutine -> runCoroutine()
            R.id.vRunRx -> runRx()
            R.id.vRunBenchmark -> Benchmark(this).run()
        }
        Toast.makeText(this, R.string.look_logs, Toast.LENGTH_SHORT).show()
    }

    private fun runCore() {
        pufferCore.apply {
            Log.i(TAG_CORE, "Cleaning database...")
            removeAll()

            Log.i(TAG_CORE, "Adding new values...")
            put(KEY_DOUBLE, VALUE_DOUBLE)
            put(KEY_DOUBLE_LIST, VALUE_DOUBLE_LIST)
            put(KEY_FLOAT, VALUE_FLOAT)
            put(KEY_FLOAT_LIST, VALUE_FLOAT_LIST)
            put(KEY_INT, VALUE_INT)
            put(KEY_INT_LIST, VALUE_INT_LIST)
            put(KEY_LONG, VALUE_LONG)
            put(KEY_LONG_LIST, VALUE_LONG_LIST)
            put(KEY_BOOLEAN, VALUE_BOOLEAN)
            put(KEY_BOOLEAN_LIST, VALUE_BOOLEAN_LIST)
            put(KEY_STRING, VALUE_STRING)
            put(KEY_STRING_LIST, VALUE_STRING_LIST)

            Log.i(TAG_CORE, "Database dump:")
            getKeys().forEach { key ->
                Log.i(TAG_CORE, "$key -> ${get<Any>(key)}")
            }
        }
    }

    private fun runCoroutine() = launch {
        pufferCoroutine.apply {
            Log.i(TAG_COROUTINES, "Cleaning database...")
            removeAll()

            Log.i(TAG_COROUTINES, "Adding new values...")
            put(KEY_DOUBLE, VALUE_DOUBLE)
            put(KEY_DOUBLE_LIST, VALUE_DOUBLE_LIST)
            put(KEY_FLOAT, VALUE_FLOAT)
            put(KEY_FLOAT_LIST, VALUE_FLOAT_LIST)
            put(KEY_INT, VALUE_INT)
            put(KEY_INT_LIST, VALUE_INT_LIST)
            put(KEY_LONG, VALUE_LONG)
            put(KEY_LONG_LIST, VALUE_LONG_LIST)
            put(KEY_BOOLEAN, VALUE_BOOLEAN)
            put(KEY_BOOLEAN_LIST, VALUE_BOOLEAN_LIST)
            put(KEY_STRING, VALUE_STRING)
            put(KEY_STRING_LIST, VALUE_STRING_LIST)

            Log.i(TAG_COROUTINES, "Database dump:")
            getKeys().forEach { key ->
                Log.i(TAG_COROUTINES, "$key -> ${get<Any>(key)}")
            }
        }
    }

    private fun runRx() {
        Log.i(TAG_RX, "Cleaning database...")
        val disposable = pufferRx.removeAll()
            .doOnComplete { Log.i(TAG_RX, "Adding new values...") }
            .andThen(pufferRx.put(KEY_DOUBLE, VALUE_DOUBLE))
            .andThen(pufferRx.put(KEY_DOUBLE_LIST, VALUE_DOUBLE_LIST))
            .andThen(pufferRx.put(KEY_FLOAT, VALUE_FLOAT))
            .andThen(pufferRx.put(KEY_FLOAT_LIST, VALUE_FLOAT_LIST))
            .andThen(pufferRx.put(KEY_INT, VALUE_INT))
            .andThen(pufferRx.put(KEY_INT_LIST, VALUE_INT_LIST))
            .andThen(pufferRx.put(KEY_LONG, VALUE_LONG))
            .andThen(pufferRx.put(KEY_LONG_LIST, VALUE_LONG_LIST))
            .andThen(pufferRx.put(KEY_BOOLEAN, VALUE_BOOLEAN))
            .andThen(pufferRx.put(KEY_BOOLEAN_LIST, VALUE_BOOLEAN_LIST))
            .andThen(pufferRx.put(KEY_STRING, VALUE_STRING))
            .andThen(pufferRx.put(KEY_STRING_LIST, VALUE_STRING_LIST))

            .doOnComplete { Log.i(TAG_RX, "Database dump:") }
            .andThen(pufferRx.getKeys())
            .flattenAsObservable { it }
            .map { it to pufferRx.get<Any>(it).blockingGet() }
            .toList()

            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { dump ->
                dump.forEach {
                    Log.i(TAG_RX, "${it.first} -> ${it.second}")
                }
            }

        disposables.add(disposable)
    }
}
