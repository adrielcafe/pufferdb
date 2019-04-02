package cafe.adriel.pufferdb.core

class PufferException(
    message: String,
    throwable: Throwable? = null
) : RuntimeException(message, throwable)
