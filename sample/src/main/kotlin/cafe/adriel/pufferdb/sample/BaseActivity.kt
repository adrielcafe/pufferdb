package cafe.adriel.pufferdb.sample

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class BaseActivity : AppCompatActivity() {

    protected val mainScope = MainScope()
    protected val disposables = CompositeDisposable()

    override fun onDestroy() {
        mainScope.cancel()
        disposables.dispose()
        super.onDestroy()
    }
}
