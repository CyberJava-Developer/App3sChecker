package gsm.gsmnetindo.app_3s_checker.ui.main.result.location

import android.content.Context
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.data.network.response.barcode.Location
import gsm.gsmnetindo.app_3s_checker.internal.LocalDateTimeParser
import kotlinx.android.synthetic.main.item_location.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.util.*
import kotlin.collections.ArrayList

class LocationItem(
    private val location: Location,
    private val context: Context
): Item() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun bind(viewHolder: ViewHolder, position: Int) {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(location.latitude.toDouble(), location.longitude.toDouble(), 1)
        val cityName = addresses[0].getAddressLine(0)
        val stateName = addresses[0].getAddressLine(1)
        val countryName = addresses[0].getAddressLine(2)
        val update = LocalDateTimeParser.utcToLocal(location.updatedAt)
        val create = LocalDateTimeParser.utcToLocal(location.createdAt)
        val formateupdate = dateTimeDisplay(
            update.toLocalDateTime().toString()
        )
        val formateCreate = dateTimeDisplay(
            create.toLocalDateTime().toString()
        )
        viewHolder.apply {
            SetTitle.text = formateCreate
            LocationTrack.text = "${cityName}\n${formateupdate}"
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun dateTimeDisplay(dateTime: String): String {
        val rawDateTime = LocalDateTime.parse(dateTime)
        val zonedDateTime = ZonedDateTime.of(
            rawDateTime.toLocalDate(),
            rawDateTime.toLocalTime(),
            ZoneId.systemDefault()
        )
        val day =
            zonedDateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ID"))
        val month = zonedDateTime.month.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ID"))
        return "${zonedDateTime.dayOfMonth} $month ${zonedDateTime.year}"
    }

    override fun getLayout() = R.layout.item_location
}