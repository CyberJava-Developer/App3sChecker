package gsm.gsmnetindo.app_3s_checker.ui.main.result.location

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.HttpException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.data.network.response.barcode.Location
import gsm.gsmnetindo.app_3s_checker.internal.LocalDateTimeParser
import gsm.gsmnetindo.app_3s_checker.internal.NoConnectivityException
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.item_location.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.TextStyle
import java.util.*
import java.util.concurrent.TimeoutException


class LocationItem(
    private val location: Location,
    private val context: Context
): Item() {

    private lateinit var mMap: GoogleMap

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun bind(viewHolder: ViewHolder, position: Int) {
        try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(
            location.latitude.toDouble(),
            location.longitude.toDouble(),
            1
        )
        val cityName = addresses[0].getAddressLine(0)
        val stateName = addresses[0].getAddressLine(1)
        val countryName = addresses[0].getAddressLine(2)
        val update = LocalDateTimeParser.utcToLocal(location.updatedAt)
        val create = LocalDateTimeParser.utcToLocal(location.createdAt)
        val myLocation : LatLng = LatLng(
            location.latitude.toDouble(),
            location.longitude.toDouble()
        )
        val formateupdate = dateTimeDisplay(
            update.toLocalDateTime().toString()
        )
        val formateCreate = dateTimeDisplay(
            create.toLocalDateTime().toString()
        )

        viewHolder.apply {
                SetTitle.text = formateCreate
                LocationTrack.text = "${cityName}, ${countryName}"
                Coordinate.text = "${location.latitude}, ${location.longitude}"
                mapView.onCreate(Bundle())
                val options: GoogleMapOptions = GoogleMapOptions().liteMode(true)
                options.liteMode.apply {
                    mapView.getMapAsync { googleMap ->
                        googleMap.addMarker(
                            MarkerOptions().position(myLocation).title(cityName)
                        )
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f))
                        mapView.onResume()
                    }
                }

            copybtn.setOnClickListener {
//                val textToCopy = Coordinate.text
//
//                val clipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
//                val clipData = ClipData.newPlainText("text", textToCopy)
//                clipboardManager.setPrimaryClip(clipData)
//
//                Toast.makeText(context, "${Coordinate.text} Copy to Clipboard", Toast.LENGTH_LONG).show()
                // Creates an Intent that will load a map of San Francisco
                val gmmIntentUri = Uri.parse("http://maps.google.com/maps?q=${location.latitude}, ${location.longitude}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                context.startActivity(mapIntent)
            }

        }
    }catch (e: Exception){
        when(e){
            is NoConnectivityException ->{
                Log.d("Internet", "Internet Tidak Tersedia")
            }
            is HttpException->{
                Log.d("Internet", "Kesalahan")
            }
            is TimeoutException ->{
                Log.d("Koneksi", "Time Out")
            }else -> {
            Log.d("Kesalahan", "${e.message}")
        }
        }
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
        return "$day, ${zonedDateTime.dayOfMonth} $month ${zonedDateTime.year} ${zonedDateTime.toLocalTime()}"
    }

    private fun bitmapDescriptorFromVector(
        context: Context,
        @DrawableRes vectorDrawableResourceId: Int
    ): BitmapDescriptor? {
        val background =
            ContextCompat.getDrawable(context, vectorDrawableResourceId)
        background!!.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)
        val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId)
        vectorDrawable!!.setBounds(
            40,
            20,
            vectorDrawable.intrinsicWidth + 40,
            vectorDrawable.intrinsicHeight + 20
        )
        val bitmap = Bitmap.createBitmap(
            background.intrinsicWidth,
            background.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        background.draw(canvas)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
    override fun getLayout() = R.layout.item_location
//    override fun onMapReady(googleMap: GoogleMap) {
//        val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.google_style)
//        googleMap.uiSettings.isZoomControlsEnabled = true
//        googleMap.setPadding(10, 10, 10, 200)
//
//        mMap = googleMap
//        mMap.setMapStyle(mapStyleOptions)
//    }
}