package gsm.gsmnetindo.app_3s_checker.ui.Intro

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.internal.ScopedActivity
import gsm.gsmnetindo.app_3s_checker.ui.login.LoginActivity
import gsm.gsmnetindo.app_3s_checker.ui.main.MainActivity
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModel
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModelFactory
import kotlinx.android.synthetic.main.activity_introslide.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

const val PERMISSION_ID = 42
class IntroActivity : ScopedActivity(), KodeinAware {

    override val kodein by closestKodein()
    private val introViewModelFactory: IntroViewModelFactory by instance()
    private val accountViewModelFactory: AccountViewModelFactory by instance()
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var viewModelProvider : IntroViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_introslide)
        Log.i("Activity Started", "Splash Activity")
        installerBind()
        checkPermission()

        viewpage.adapter = introslideadapter(supportFragmentManager)
        dot()
        buttonNextLayout()
    }
    private fun checkPermission(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
//                start()
            }
        } else {
            askPermission()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun askPermission() {
        val pdialod = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Izin Akses")
            .setContentText("Agar aplikasi ${getString(R.string.app_name)} bisa berjalan normal, kami membutuhkan izin untuk mengakses: SMS, Kamera, Lokasi, dan Penyimpanan")
            .setConfirmText("Izinkan")
            .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation()
                requestPermissions()
            }
        pdialod.setCancelable(false)
        pdialod.show()
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            PERMISSION_ID
        ).also {
//            start()
        }
    }
    private fun buttonNextLayout() {
        val btn = findViewById<Button>(R.id.nextbtn)
        viewpage.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
                if (getItem(1) == 3){
                    nextbtn.text = "Selesai"
                } else {
                    nextbtn.text = "Lanjut"
                }
                Log.d("onPageScrollStateChangd", state.toString())
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                Log.d("onPageScrolled", getItem(+0).toString())
            }

            override fun onPageSelected(position: Int) {
                if (position == 3) Log.d("onPageSelected", getItem(+0).toString())
            }

        })

        btn.setOnClickListener {

            if (getItem(+1) != 3){
                viewpage.setCurrentItem(getItem(+1), true)
            }
            else{
                //setFirstInstall()
                // intro end
                viewModelProvider.setFirst()
                Log.d("set First", viewModelProvider.isFirst().value.toString())

//                startActivity(Intent(this, loginverification::class.java))
//                finish()
                toLogin()
            }
        }
    }

    private fun getItem(i: Int): Int {
        return viewpage.currentItem + i
    }

    private fun dot() {
        val dotsIndicator = findViewById<DotsIndicator>(R.id.dots_indicator)
        val viewPager = findViewById<ViewPager>(R.id.viewpage)
        val adapter = introslideadapter(supportFragmentManager)
        viewPager.adapter = adapter
        dotsIndicator.setViewPager(viewPager)
    }

    private fun installerBind() {
        viewModelProvider =  ViewModelProvider(this, introViewModelFactory).get(IntroViewModel::class.java)
    }

    private fun setFirstInstall(){
        viewModelProvider.setFirst()
    }
    private fun isLoggedIn(){
        accountViewModel.isLoggedIn().observe(this, Observer {
            if (it){
                toMain()
            } else {
                toLogin()
            }
        })
    }
    private fun toMain(){
        Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(this)
            finish()
        }
    }
    private fun toLogin(){
        Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(this)
            finish()
        }
    }
}