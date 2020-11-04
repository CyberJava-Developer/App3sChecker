package gsm.gsmnetindo.app_3s_checker.ui.dashboard.pengawas

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.load.HttpException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.data.network.response.observation.ObservationResponse
import gsm.gsmnetindo.app_3s_checker.data.network.response.observation.Status
import gsm.gsmnetindo.app_3s_checker.internal.LocalDateTimeParser
import gsm.gsmnetindo.app_3s_checker.internal.LocalDateTimeParser.toMoment
import gsm.gsmnetindo.app_3s_checker.internal.NoConnectivityException
import gsm.gsmnetindo.app_3s_checker.internal.ScopedFragment
import gsm.gsmnetindo.app_3s_checker.ui.main.MainViewModel
import gsm.gsmnetindo.app_3s_checker.ui.main.MainViewModelFactory
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModel
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModelFactory
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.BarcodeViewModel
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.BarcodeViewModelFactory
import kotlinx.android.synthetic.main.activity_pengawas.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import org.threeten.bp.ZonedDateTime
import java.util.concurrent.TimeoutException


class Fragment_pengawas : ScopedFragment(), OnMapReadyCallback, KodeinAware {
    override val kodein by closestKodein()

    private val barcodeViewModelFactory: BarcodeViewModelFactory by instance()
    private lateinit var barcodeViewModel: BarcodeViewModel
    private val mainViewModelFactory: MainViewModelFactory by instance()
    private lateinit var mainViewModel: MainViewModel
    private val accountViewModelFactory: AccountViewModelFactory by instance()
    private lateinit var accountViewModel: AccountViewModel

    private lateinit var mMap: GoogleMap
    private lateinit var myLocation: LatLng

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        barcodeViewModel =
            ViewModelProvider(this, barcodeViewModelFactory).get(BarcodeViewModel::class.java)
        accountViewModel =
            ViewModelProvider(this, accountViewModelFactory).get(AccountViewModel::class.java)
        val view = inflater.inflate(R.layout.activity_pengawas, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            onMapReady(it)
        }
        mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)

        return view
    }

    private fun loadData() = launch {
        try {
            mMap.clear()
            val me = accountViewModel.getPhonePref()
            barcodeViewModel.observation().observe(viewLifecycleOwner, { all ->
                observerables = all
                all.map {
                    if (it.updatedAt != null) {
                        if (it.user.account.phone != me) {
                            val timeAgo = ZonedDateTime.now()
                                .toEpochSecond() - LocalDateTimeParser.utcToLocal(
                                it.updatedAt
                            ).toEpochSecond()
                            Log.i(
                                "addMarker",
                                "${it.user.name} ${it.user.status} in {${it.latitude} ${it.longitude}} ${
                                    LocalDateTimeParser.utcToLocal(
                                        it.updatedAt
                                    ).toMoment()
                                }($timeAgo)"
                            )
                            var colorRadius = 0
                            val icon = when (it.user.status.status) {
                                0 -> {
                                    colorRadius = 0x2232A86D //Color.parseColor("#32a86d")
                                    R.drawable.circlemapme
                                }
                                in 25..75 -> {
                                    colorRadius = 0x22FFE369 //Color.parseColor("#FFE369")
                                    R.drawable.circlemapgreen
                                }
                                in 100..175 -> {
                                    colorRadius = 0x22e60000 //Color.parseColor("#e60000")
                                    R.drawable.circlemap
                                }
                                else -> R.drawable.circlemapme
                            }
                            mMap.addMarker(
                                MarkerOptions().position(LatLng(it.latitude, it.longitude))
                                    .title("${it.user.name}")
                                    .snippet(
                                        "terakhir dilihat ${
                                            LocalDateTimeParser.utcToLocal(it.updatedAt).toMoment()
                                        }"
                                    )
                                    .icon(
                                        bitmapDescriptorFromVector
                                            (requireContext(), icon)
                                    )
                                    .flat(true)
                            )
                            mMap.addCircle(
                                CircleOptions()
                                    .radius(it.accuracy)
                                    .center(LatLng(it.latitude, it.longitude))
                                    .strokeWidth(0.1f)
                                    .strokeColor(0x22+colorRadius)
                                    .fillColor(0x22+colorRadius)
//                                    .zIndex(0.1f)
                            )

                            val status = it.user.status
                            val lang = "${it.latitude}, ${it.longitude}"
                            mMap.setOnMapClickListener {
                                infowindow.visibility = View.GONE
                            }
                            mMap.setOnMarkerClickListener { marker ->
                                marker.showInfoWindow()
                                Log.d("GoogleMap", " click")
                                //focus the marker
                                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.position))
                                displayCustomeInfoWindow(marker)
                                true
                            }
                        }
                    }
                }
            })
        } catch (e: Exception) {
            when(e){
                is NoConnectivityException->{Log.d("Internet", "Internet Tidak Tersedia")}
                is TimeoutException ->{Log.d("Internet", "Terlalu lama Menunggu Koneksi Anda")}
                is HttpException -> {Log.d("Internet", "Kesalahan Jaringan")}
                else ->{
                    Log.d("Internet", "${e.message}")
                }
            }
        }
    }
    private lateinit var observerables: ObservationResponse

    private fun displayCustomeInfoWindow(marker: Marker) {
        infowindow.visibility = View.VISIBLE
        namauser.text = marker.title
        var latLng = ""
        observerables.map {
            if (marker.title == it.user.name){
                setUserStatus(it.user.status)
                latLng = "${it.latitude},${it.longitude}"
                return@map
            }
        }
        lang_user.text = latLng
        user_last_location.text = marker.snippet
        infowindow.setOnClickListener {
            val gmmIntentUri = Uri.parse("http://maps.google.com/maps?q=${latLng}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            context?.startActivity(mapIntent)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        try {
            val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.google_style)
            googleMap.uiSettings.isZoomControlsEnabled = true
            googleMap.setPadding(10, 10, 10, 200)

            mMap = googleMap
            mMap.setMapStyle(mapStyleOptions)

            loadData()
            myLocation()
// Add some markers to the map, and add a data object to each marker.
        }catch (e:Exception){
            when(e){
                is NoConnectivityException->{Log.d("Internet", "Internet Tidak Tersedia")}
                is TimeoutException ->{Log.d("Internet", "Terlalu lama Menunggu Koneksi Anda")}
                is HttpException -> {Log.d("Internet", "Kesalahan Jaringan")}
                else ->{
                    Log.d("Internet", "${e.message}")
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun myLocation() = launch {
        try {
            mMap.isMyLocationEnabled = true
            mMap.isBuildingsEnabled = true
            mMap.setOnMyLocationClickListener {

            }
            mainViewModel.getLocation().observe(viewLifecycleOwner, Observer {
                val liveLocation = LatLng(it.latitude, it.longitude)
                myLocation = liveLocation
//            mMap.addMarker(
//                MarkerOptions().position(myLocation).title("Lokasi saya")
//                    .icon(bitmapDescriptorFromVector(
//                        requireContext(), R.drawable.circlemapme
//                    ))
//            )
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f))

                // Zoom in, animating the camera.
                mMap.animateCamera(CameraUpdateFactory.zoomIn())

// Zoom out to zoom level 10, animating with a duration of 2 seconds.
                mMap.animateCamera(CameraUpdateFactory.zoomTo(20f), 2000, null)

//// Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
//                val cameraPosition = CameraPosition.Builder()
//                    .target(myLocation) // Sets the center of the map to Mountain View
//                    .zoom(17f)            // Sets the zoom
//                    .bearing(90f)         // Sets the orientation of the camera to east
//                    .tilt(30f)            // Sets the tilt of the camera to 30 degrees
//                    .build()              // Creates a CameraPosition from the builder
//                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            })
        }catch (e:Exception){
            when(e){
                is NoConnectivityException->{Log.d("Internet", "Internet Tidak Tersedia")}
                is TimeoutException ->{Log.d("Internet", "Terlalu lama Menunggu Koneksi Anda")}
                is HttpException -> {Log.d("Internet", "Kesalahan Jaringan")}
                else ->{
                    Log.d("Internet", "${e.message}")
                }
            }
        }
    }

    //change icon circle
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


    private fun setUserStatus(status: Status) {
        when (status.status) {
            0 -> {
                status_user.text = "SEHAT"
                status_user.setTextColor(Color.parseColor("#32a86d"))
            }
            in 25..75 -> {
                status_user.text = "BERESIKO"
                status_user.setTextColor(Color.parseColor("#e67e00"))
            }
            in 100..175 -> {
                status_user.text = "POSITIF"
                status_user.setTextColor(Color.parseColor("#e60000"))
            }
            else -> {
                status_user.text = "---"
                status_user.setTextColor(Color.parseColor("#000000"))
            }
        }
    }
}
