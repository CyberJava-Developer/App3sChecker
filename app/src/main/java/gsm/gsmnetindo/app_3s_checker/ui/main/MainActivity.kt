package gsm.gsmnetindo.app_3s_checker.ui.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.internal.LocationNotEnabledException
import gsm.gsmnetindo.app_3s_checker.internal.LocationPermissionException
import gsm.gsmnetindo.app_3s_checker.internal.ScopedActivity
import gsm.gsmnetindo.app_3s_checker.ui.dashboard.Fragment_QRcode
import gsm.gsmnetindo.app_3s_checker.ui.dashboard.Fragment_akun
import gsm.gsmnetindo.app_3s_checker.ui.dashboard.Fragment_pengawas
import gsm.gsmnetindo.app_3s_checker.ui.dashboard.Fragment_pesan
import gsm.gsmnetindo.app_3s_checker.ui.dashboard.home.HomeFragment
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModel
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.net.SocketTimeoutException
import kotlin.system.exitProcess

class MainActivity : ScopedActivity(), KodeinAware {
    override val kodein by closestKodein()
    private val mainViewModelFactory: MainViewModelFactory by instance()
    private val accountViewModelFactory: AccountViewModelFactory by instance()
    private lateinit var mainViewModel: MainViewModel
    private lateinit var accountViewModel: AccountViewModel

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val navView = findViewById<BottomNavigationView>(R.id.navbar)
//        navView.menu = MenuInflater(this).inflate(R.menu.dashboard_menu_role_2, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)
        accountViewModel = ViewModelProvider(this, accountViewModelFactory).get(AccountViewModel::class.java)
        izincamera()

        val navView = findViewById<BottomNavigationView>(R.id.navbar)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentcontainer,
                HomeFragment()
            ).commit()
        }
        getLocation()
        when(accountViewModel.getRolePref()) {
            2 -> { navbar.inflateMenu(R.menu.dashboard_menu_role_2) }
            3 -> { navbar.inflateMenu(R.menu.dashboard_menu_role_3) }
            4 or 7 -> { navbar.inflateMenu(R.menu.dashboard_menu_role_4_7) }
            else -> { navbar.inflateMenu(R.menu.dashboard_menu_role_else) }
        }
//        navbar.inflateMenu(R.menu.dashboard_menu_role_2)
        navView.setOnNavigationItemSelectedListener { menuItem ->
            try {
                var frg: Fragment? = null
                when (menuItem.itemId) {
                    R.id.homedashboard -> frg =
                        HomeFragment()
                    R.id.pesan -> frg = Fragment_pesan()
                    R.id.barcode -> frg = Fragment_QRcode()
                    R.id.akun -> frg = Fragment_akun()
                    R.id.pengawas -> frg = Fragment_pengawas()
                }
                if (frg != null) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentcontainer, frg)
                        .commit()
                }
            }catch (e: Exception){
                when(e){
                    is SocketTimeoutException -> {
                        alern()
                        //Toast.makeText(this@Splash, "Tidak bisa menyambung ke server, coba beberapa saat lagi", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        //Toast.makeText(this@SplashActivity, e.message, Toast.LENGTH_LONG).show()
                        alern()
                    }
                }
            }
            true
        }
    }

    private fun alern() {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Koneksi Habis")
            .setContentText("${getString(R.string.app_name)} Membutuhkan Terlalu banyak waktu untuk merespon. Coba Periksa koneksi internet anda")
            .setConfirmText("OK")
            .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation()
                exitProcess(0)
            }
            .show()
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
            mainViewModel.getAddress().observe(this@MainActivity, {
                supportActionBar?.subtitle = "${it.subLocality}, ${it.subAdminArea}"
                Log.i("getAddressMain", "$it")
            })
        } catch (e: Exception){
            when(e){
                is LocationPermissionException -> {
                    requestLocation()
                }
                is LocationNotEnabledException -> {
                    Toast.makeText(this@MainActivity, "Mohon hidupkan GPS anda", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
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
        }
//        else {
            // Permission has already been granted
//        }
    }
}