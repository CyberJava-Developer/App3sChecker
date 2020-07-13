package gsm.gsmnetindo.app_3s_checker.ui.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.ui.dashboard.*
import gsm.gsmnetindo.app_3s_checker.ui.dashboard.home.HomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        izincamera()

        val navview = findViewById<BottomNavigationView>(R.id.navbar)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentcontainer,
                HomeFragment()
            ).commit()
        }
        navview.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
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
                supportFragmentManager.beginTransaction().replace(R.id.fragmentcontainer, frg).commit()
            }
            true
        })
    }

    fun izincamera(){
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