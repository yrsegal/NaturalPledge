package com.wiresegal.naturalpledge.common.core.helper

import org.apache.logging.log4j.Level
import com.wiresegal.naturalpledge.common.NaturalPledge

/**
 * @author WireSegal
 * Created at 7:35 PM on 6/13/16.
 */
object NPLogger {

    val coreLog = NaturalPledge.LOGGER

    fun log(level: Level, format: String, vararg data: Any) = coreLog.log(level, format, *data)

    fun log(level: Level, ex: Throwable, format: String, vararg data: Any) = coreLog.log(level, format.format(*data), ex)

    fun severe(format: String, vararg data: Any) = log(Level.ERROR, format, *data)

    fun warning(format: String, vararg data: Any) = log(Level.WARN, format, *data)

    fun info(format: String, vararg data: Any) = log(Level.INFO, format, *data)

    fun fine(format: String, vararg data: Any) = log(Level.DEBUG, format, *data)

    fun finer(format: String, vararg data: Any) = log(Level.TRACE, format, *data)

    fun bigWarning(format: String, vararg data: Any) {
        val trace = Thread.currentThread().stackTrace
        log(Level.WARN, "****************************************")
        log(Level.WARN, "* " + format, *data)
        var i = 2
        while (i < 8 && i < trace.size) {
            log(Level.WARN, "*  at %s%s", trace[i].toString(), if (i == 7) "..." else "")
            i++
        }
        log(Level.WARN, "****************************************")
    }
}
