package gsm.gsmnetindo.app_3s_checker.ui.dashboard

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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.data.network.response.observation.Status
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
        barcodeViewModel = ViewModelProvider(this, barcodeViewModelFactory).get(BarcodeViewModel::class.java)
        accountViewModel = ViewModelProvider(this, accountViewModelFactory).get(AccountViewModel::class.java)
        val view = inflater.inflate(R.layout.activity_pengawas, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)

        return view
    }
    private fun loadData() = launch {
        try {
            mMap.clear()
            val me = accountViewModel.getPhonePref()
            barcodeViewModel.observation().observe(viewLifecycleOwner, { all ->
                all.map {
                    if (it.user.account.phone != me) {
                        Log.i(
                            "addMarker",
                            "${it.user.name} ${it.user.status} in ${it.latitude} - ${it.longitude} - ${it.user.account.avatar}"
                        )
                        mMap.addMarker(
                            MarkerOptions().position(LatLng(it.latitude, it.longitude))
                                .title("${it.user.name}")
                                .icon(
                                    bitmapDescriptorFromVector
                                        (requireContext(), R.drawable.circlemap)
                                )
                        )

                        val status = it.user.status
                        val lang = "${it.latitude}, ${it.longitude}"
                        mMap.setOnMarkerClickListener(OnMarkerClickListener { marker ->
                            Log.d("GoogleMap", " click")
                            //focus the market
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.position))
                            if (infowindow.visibility == View.VISIBLE) infowindow.visibility =
                                View.GONE else displayCustomeInfoWindow(
                                marker, status, lang
                            )
                            true
                        })
                    }
                }
            })
        } catch (e: Exception){
            Log.e("load data pengawas", "${e.message}", e)
        }
    }

    private fun displayCustomeInfoWindow(marker: Marker, status: Status, lang: String) {
        infowindow.visibility = View.VISIBLE
        namauser.text = marker.title
        setUserStatus(status)
        lang_user.text = lang

        infowindow.setOnClickListener {
            val gmmIntentUri = Uri.parse("http://maps.google.com/maps?q=${lang}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            context?.startActivity(mapIntent)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.setPadding(10, 10, 10, 200)

        mMap = googleMap
        // Add a marker in surabaya and move the camera
        val surabaya = LatLng(-7.278030, 112.764384)

        mMap.moveCamera(CameraUpdateFactory.newLatLng(surabaya))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(surabaya, 15f))
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn())
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15f), 2000, null)
        loadData()
        myLocation()

// Add some markers to the map, and add a data object to each marker.

    }
    @SuppressLint("MissingPermission")
    private fun myLocation() = launch {
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
        })
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
            "negative" -> {
                status_user.text = "SEHAT"
                status_user.setTextColor(Color.parseColor("#32a86d"))
            }
            "odp" -> {
                status_user.text = "BERESIKO"
                status_user.setTextColor(Color.parseColor("#dae600"))
            }
            "pdp" -> {
                status_user.text = "BERESIKO"
                status_user.setTextColor(Color.parseColor("#e67e00"))
            }
            "positive" -> {
                status_user.text = "POSITIF"
                status_user.setTextColor(Color.parseColor("#e60000"))
            }
            else -> {
                status_user.text = "Tidak Terverifikasi"
                status_user.setTextColor(Color.parseColor("#00c5e3"))
            }
        }
    }
}
