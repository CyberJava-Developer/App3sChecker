package gsm.gsmnetindo.app_3s_checker.internal

import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

class DateTimeParser {
    fun parse(rawDate: String): ZonedDateTime {
        val date = rawDate.substring(0, 10)
        val time = rawDate.substring(11, 19)
        val localDateTime = LocalDateTime.parse("${date}T${time}").plusHours(7)
        return ZonedDateTime.of(localDateTime.toLocalDate(), localDateTime.toLocalTime(), ZoneId.of("UTC"))
    }
}