package cafe.adriel.pufferdb.android

import android.content.Context
import cafe.adriel.pufferdb.core.Puffer
import cafe.adriel.pufferdb.core.PufferDB
import java.io.File

object AndroidPufferDB {
    private const val DEFAULT_FILE_NAME = "puffer.db"

    private lateinit var defaultInstance: Puffer

    fun withDefault(context: Context): Puffer {
        if (!::defaultInstance.isInitialized) {
            val pufferFile = getInternalFile(context, DEFAULT_FILE_NAME)
            defaultInstance = PufferDB.with(pufferFile)
        }
        return defaultInstance
    }

    fun getInternalFile(context: Context, fileName: String) = File(context.filesDir, fileName)
}
