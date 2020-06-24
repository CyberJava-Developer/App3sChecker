package gsm.gsmnetindo.app_3s_checker.internal

import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

object LocalDateTimeParser {
    fun utcToLocal(utcDateTime: String): ZonedDateTime {
        return ZonedDateTime.parse(utcDateTime).withZoneSameInstant(ZoneId.systemDefault())
    }

    fun localToUtc(zonedDateTime: ZonedDateTime): ZonedDateTime {
        return ZonedDateTime.ofInstant(zonedDateTime.toInstant(), ZoneId.of("UTC"))
    }

    fun nowOfUtc(): ZonedDateTime {
        return ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"))
    }

    fun nowOfLocal(): ZonedDateTime {
        return ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())
    }
}