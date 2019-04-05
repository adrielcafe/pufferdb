package cafe.adriel.pufferdb.sample

import android.app.Application
import com.orhanobut.hawk.Hawk
import com.tencent.mmkv.MMKV
import io.paperdb.Paper

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        Paper.init(this)
        Hawk.init(this).build()
    }
}
