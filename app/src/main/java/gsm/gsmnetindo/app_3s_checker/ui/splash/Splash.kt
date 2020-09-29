package gsm.gsmnetindo.app_3s_checker.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import gsm.gsmnetindo.app_3s_checker.BuildConfig
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.internal.NoConnectivityException
import gsm.gsmnetindo.app_3s_checker.internal.ScopedActivity
import gsm.gsmnetindo.app_3s_checker.ui.Intro.IntroActivity
import gsm.gsmnetindo.app_3s_checker.ui.login.LoginActivity
import gsm.gsmnetindo.app_3s_checker.ui.main.MainActivity
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModel
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModelFactory
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.net.ConnectException
import java.net.SocketTimeoutException
import kotlin.system.exitProcess

class Splash : ScopedActivity(), KodeinAware {

    override val kodein by closestKodein()
    private val splashViewModelFactory: SplashViewModelFactory by instance()
    private lateinit var splashViewModel: SplashViewModel
    private val accountViewModelFactory: AccountViewModelFactory by instance()
    private lateinit var accountViewModel: AccountViewModel

    lateinit var topanim:Animation
    lateinit var buttomanim:Animation
    lateinit var image:ImageView
    lateinit var logotext:ImageView
    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        splashViewModel = ViewModelProvider(this, splashViewModelFactory).get(SplashViewModel::class.java)
        accountViewModel = ViewModelProvider(this, accountViewModelFactory).get(AccountViewModel::class.java)

        topanim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        buttomanim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)
        image = findViewById(R.id.Logo)
        logotext = findViewById(R.id.textlogo)

        image.animation = topanim
        logotext.animation = buttomanim

        handler = Handler()
        handler.postDelayed({
            checkNetwork()
//            val intent = Intent(this, IntroActivity::class.java)
//            startActivity(intent)
//            finish()
        }, 5000)
    }
    private fun checkNetwork(){
        if (splashViewModel.isOnline().not()){
            alerndialog()
            Toast.makeText(this,"tidak ada koneksi internet", Toast.LENGTH_LONG).show();
            //finish()
        } else {
            checkUpdate()
        }
    }

    fun alerndialog(){
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Kesalahan Jaringan")
            .setContentText("internet koneksi buruk, pastikan koneksi anda stabil dan silakan coba kembali")
            .setConfirmText("Terima Kasih")
            .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation()
                exitProcess(0)
            }
            .show()
    }

    private fun checkUpdate() = launch {
        try {
            splashViewModel.getLatestVersion().observe(this@Splash, Observer {
                val currentVersion = BuildConfig.VERSION_CODE
                if (currentVersion < it.appVersionCode) {
                    SweetAlertDialog(this@Splash, SweetAlertDialog.SUCCESS_TYPE).apply {
                        setTitleText("Versi terbaru telah rilis")
                        .setContentText("Mohon update ${it.appName} ke versi ${it.appVersionName}")
                        .setConfirmText("OK")
                        .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation()
                            exitProcess(0)
                        }
                        .show()
                    }

                } else {
                    isFirstTime()
                }
            })
        } catch (e: Exception) {
            Log.e("version update", e.message, e)
            when(e) {
                is NoConnectivityException -> {
                    alern()
                }
                is SocketTimeoutException -> {
                    alern()
                    //Toast.makeText(this@Splash, "Tidak bisa menyambung ke server, coba beberapa saat lagi", Toast.LENGTH_LONG).show()
                }
                is ConnectException -> {
                    alern()
                }
                else -> {
                    Toast.makeText(this@Splash, e.message, Toast.LENGTH_LONG).show()
                }
            }
            //finish()
        }
    }

    fun alern(){
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Kesalahan Jaringan")
            .setContentText("internet koneksi buruk, pastikan koneksi anda stabil dan silakan coba kembali")
            .setConfirmText("Terima Kasih")
            .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation()
                exitProcess(0)
            }
            .show()

    }
    private fun isFirstTime() {
        try {
            splashViewModel.isFirst().observe(this, Observer {
                if (it){
                    // to intro
                    Intent(this, IntroActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(this)
                        finish()
                    }
                } else {
                    // is logged in
                    isLoggedIn()
                }
            })
        }catch (e: Exception){
            when(e) {
                is NoConnectivityException -> {
                    alern()
                }
                is SocketTimeoutException -> {
                    alern()
                    //Toast.makeText(this@Splash, "Tidak bisa menyambung ke server, coba beberapa saat lagi", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(this@Splash, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }

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
        accountViewModel.getRolePref().apply {
            if (this >= 2) {
                Intent(this@Splash, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(this)
                    finish()
                }
            } else {
                Toast.makeText(this@Splash, "anda tidak memiliki izin untuk login", Toast.LENGTH_LONG).show()
            }
        }
//        Intent(this, MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(this)
//            finish()
//        }
    }
    private fun toLogin(){
        Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(this)
            finish()
        }
    }
}