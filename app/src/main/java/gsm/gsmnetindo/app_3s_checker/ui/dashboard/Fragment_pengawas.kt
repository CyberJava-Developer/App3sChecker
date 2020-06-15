package gsm.gsmnetindo.app_3s_checker.ui.dashboard

import android.graphics.BitmapFactory
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import gsm.gsmnetindo.app_3s_checker.R
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class Fragment_pengawas : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.activity_pengawas, container, false)

        val ctx = activity!!.applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID;
        val map = view.findViewById<MapView>(R.id.mapview)

        map.setUseDataConnection(true)
        //val map = view.findViewById(R.id.map) as MapView
        map.setTileSource(TileSourceFactory.MAPNIK)
        //map.setBuiltInZoomControls(true) //Map ZoomIn/ZoomOut Button Visibility
        map.setMultiTouchControls(true)
        val mapController: IMapController
        mapController = map.controller

        //mapController.zoomTo(14, 1)
        mapController.setZoom(14)

        val mGpsMyLocationProvider = GpsMyLocationProvider(activity)
        val mLocationOverlay = MyLocationNewOverlay(mGpsMyLocationProvider, map)
        mLocationOverlay.enableMyLocation()
        mLocationOverlay.enableFollowLocation()

        val icon = BitmapFactory.decodeResource(resources, R.drawable.ic_loc)
        mLocationOverlay.setPersonIcon(icon)
        map.overlays.add(mLocationOverlay)

        mLocationOverlay.runOnFirstFix {
            map.overlays.clear()
            map.overlays.add(mLocationOverlay)
            mapController.animateTo(mLocationOverlay.myLocation)
        }
        return view
    }
}