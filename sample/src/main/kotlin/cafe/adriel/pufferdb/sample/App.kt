package cafe.adriel.pufferdb.sample

import android.app.Application
import cafe.adriel.pufferdb.android.AndroidPufferDB
import com.orhanobut.hawk.Hawk
import com.tencent.mmkv.MMKV
import io.paperdb.Paper

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidPufferDB.init(this)
        MMKV.initialize(this)
        Paper.init(this)
        Hawk.init(this).build()
    }
}
