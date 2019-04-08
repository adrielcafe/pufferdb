package cafe.adriel.pufferdb.android

import android.content.Context
import cafe.adriel.pufferdb.core.Puffer
import cafe.adriel.pufferdb.core.PufferDB
import cafe.adriel.pufferdb.core.PufferException
import java.io.File

object AndroidPufferDB {
    private const val DEFAULT_FILE_NAME = "puffer.db"

    private lateinit var puffer: Puffer
    private lateinit var filesDir: File

    fun init(context: Context) {
        filesDir = context.filesDir
        puffer = PufferDB.with(getInternalFile(DEFAULT_FILE_NAME))
    }

    fun withDefault() =
        if (::puffer.isInitialized) {
            puffer
        } else {
            throw PufferException("${this::class.java.simpleName} was not initialized")
        }

    fun getInternalFile(fileName: String) =
        if (::filesDir.isInitialized) {
            File(filesDir, fileName)
        } else {
            throw PufferException("${this::class.java.simpleName} was not initialized")
        }
}
