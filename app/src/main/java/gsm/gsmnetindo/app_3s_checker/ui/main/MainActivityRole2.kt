package gsm.gsmnetindo.app_3s_checker.ui.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.internal.LocationNotEnabledException
import gsm.gsmnetindo.app_3s_checker.internal.LocationPermissionException
import gsm.gsmnetindo.app_3s_checker.internal.ScopedActivity
import gsm.gsmnetindo.app_3s_checker.ui.dashboard.*
import gsm.gsmnetindo.app_3s_checker.ui.dashboard.home.HomeFragment
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class MainActivityRole2 : ScopedActivity(), KodeinAware {
    override val kodein by closestKodein()
    private val mainViewModelFactory: MainViewModelFactory by instance()
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_role_2)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)

        izincamera()

        val navview = findViewById<BottomNavigationView>(R.id.navbar2)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentcontainer2,
                HomeFragment()
            ).commit()
        }
        getLocation()
        navview.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            var frg: Fragment? = null
            when (menuItem.itemId) {
                R.id.homedashboard -> frg =
                    HomeFragment()
                R.id.pesan -> frg = Fragment_pesan()
                R.id.barcode -> frg = Fragment_QRcode()
                R.id.akun -> frg = Fragment_akun()
            }
            if (frg != null) {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentcontainer2, frg).commit()
            }
            true
        })
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        getLocation()
    }
    private fun getLocation() = launch {
        try {
            mainViewModel.getAddress().observe(this@MainActivityRole2, Observer {
                supportActionBar?.subtitle = "${it.subLocality}, ${it.subAdminArea}"
                Log.i("getAddressMain", "$it")
            })
        } catch (e: Exception){
            when(e){
                is LocationPermissionException -> {
                    requestLocation()
                }
                is LocationNotEnabledException -> {
                    Toast.makeText(this@MainActivityRole2, "Mohon hidupkan GPS anda", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(this@MainActivityRole2, e.message, Toast.LENGTH_LONG).show()
                    Log.e("getLocationMain", e.message, e)
                }
            }
        }
    }
    private fun requestLocation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permission = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            requestPermissions(permission, 1001)
        }
    }
    private fun izincamera(){
        //meminta izin camera
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA), Context.CONTEXT_INCLUDE_CODE)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }
}