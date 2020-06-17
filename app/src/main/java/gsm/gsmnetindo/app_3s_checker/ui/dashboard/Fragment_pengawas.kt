package gsm.gsmnetindo.app_3s_checker.ui.dashboard

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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import gsm.gsmnetindo.app_3s_checker.R


class Fragment_pengawas : Fragment(), OnMapReadyCallback{
    private lateinit var mMap: GoogleMap


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.activity_pengawas, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


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

// Add some markers to the map, and add a data object to each marker.

    }

    //change icon circle
    private fun bitmapDescriptorFromVector(
        context: Context,
        @DrawableRes vectorDrawableResourceId: Int
    ): BitmapDescriptor? {
        val background =
            ContextCompat.getDrawable(context, R.drawable.circlemap)
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
