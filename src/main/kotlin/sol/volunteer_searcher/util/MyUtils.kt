package sol.volunteer_searcher.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory


inline fun <reified T> T.logger(): ColorfulLogger {
    return ColorfulLogger(LoggerFactory.getLogger("@@-" + T::class.simpleName))
}

class ColorfulLogger(private val logger: Logger) {
    companion object {
        private const val BLACK = "\u001B[30m"
        private const val RED = "\u001B[31m"
        private const val GREEN = "\u001B[32m"
        private const val YELLOW = "\u001B[33m"
        private const val BLUE = "\u001B[34m"
        private const val MAGENTA = "\u001B[35m"
        private const val CYAN = "\u001B[36m"
        private const val WHITE = "\u001B[37m"
        private const val RESET = "\u001B[0m"
    }

    private val color = GREEN


    fun info(message: String) {
        logger.info("$color$message$RESET")
    }

    fun debug(message: String) {
        logger.debug("$color$message$RESET")
    }

    fun warn(message: String) {
        logger.warn("$color$message$RESET")
    }

    fun error(message: String) {
        logger.error("$color$message$RESET")
    }
}
