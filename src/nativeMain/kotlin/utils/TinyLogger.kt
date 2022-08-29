package utils

import kotlinx.datetime.*

class TinyLogger(private var logLevel: LogLevel = LogLevel.INFO) {
    private val timeZone: TimeZone = TimeZone.currentSystemDefault()

    fun debug(message: () -> String) {
        if (logLevel <= LogLevel.DEBUG) {
            printMessage("DEBUG", message())
        }
    }

    fun info(message: () -> String) {
        if (logLevel <= LogLevel.INFO) {
            printMessage("INFO ", message())
        }
    }

    fun error(message: () -> String) {
        if (logLevel <= LogLevel.ERROR) {
            printMessage("ERROR", message())
        }
    }

    fun error(exception: Exception, message: () -> String) {
        if (logLevel <= LogLevel.ERROR) {
            printMessage("ERROR", message() + "\n" + exception.getStackTrace().joinToString { "\n" })
        }
    }

    private fun printMessage(level: String, message: String) {
        val timestamp = Clock.System.now().toLocalDateTime(timeZone).toString()
        println("$timestamp [ $level ] $message")
    }
}

enum class LogLevel(val level: Int) {
    DEBUG(1), INFO(2), ERROR(3), NONE(4)
}