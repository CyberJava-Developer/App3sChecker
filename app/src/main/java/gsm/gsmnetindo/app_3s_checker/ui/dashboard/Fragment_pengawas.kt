package gsm.gsmnetindo.app_3s_checker.ui.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.internal.ScopedFragment
import gsm.gsmnetindo.app_3s_checker.ui.main.MainViewModel
import gsm.gsmnetindo.app_3s_checker.ui.main.MainViewModelFactory
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class Fragment_pengawas : ScopedFragment(), OnMapReadyCallback, KodeinAware {
    override val kodein by closestKodein()
    private lateinit var mMap: GoogleMap
    private val mainViewModelFactory: MainViewModelFactory by instance()
    private lateinit var mainViewModel: MainViewModel
    private lateinit var myLocation: LatLng

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.activity_pengawas, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.setPadding(10,10,10,200)

        mMap = googleMap
        // Add a marker in surabaya and move the camera
        val surabaya = LatLng(-7.278030, 112.764384)
        val surabaya2 = LatLng(-7.278445, 112.764920)

        //add penanda
        mMap.addMarker(
            MarkerOptions().position(surabaya).title("Virus Covid-19")
                .icon(bitmapDescriptorFromVector
                    (this.requireContext(), R.drawable.circlemap)))

        mMap.addMarker(
            MarkerOptions().position(surabaya2).title("Virus Covid-19")
                .icon(bitmapDescriptorFromVector
                    (this.requireContext(), R.drawable.circlemap)))

        mMap.moveCamera(CameraUpdateFactory.newLatLng(surabaya))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(surabaya, 15f))
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn())
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15f), 2000, null)
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
            mMap.addMarker(
                MarkerOptions().position(myLocation).title("Lokasi saya")
                    .icon(bitmapDescriptorFromVector(
                        requireContext(), R.drawable.circlemapme
                    ))
            )
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

}
