package gsm.gsmnetindo.app_3s_checker.internal

import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

object LocalDateTimeParser {
    private lateinit var value: ZonedDateTime
    fun utcToLocal(utcDateTime: String): ZonedDateTime {
        value = ZonedDateTime.parse(utcDateTime).withZoneSameInstant(ZoneId.systemDefault())
        return value
    }
    fun ZonedDateTime.toMoment(): String{
        return when (val secondsAgo = ZonedDateTime.now().toEpochSecond() - value.toEpochSecond()) {
            in 1..60 -> "$secondsAgo detik lalu"
            in 61..3600 -> "${secondsAgo/60} menit lalu"
            in 3601..86400 -> "${secondsAgo/3600} jam lalu"
            in 86401..604800 -> "${secondsAgo/86400} hari lalu"
            in 604801..2629743 -> "${secondsAgo/604800} minggu lalu"
            in 2629744..31556926 -> "${secondsAgo/2629743} bulan lalu"
            else -> "${secondsAgo/31556926} tahun lalu"
        }
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