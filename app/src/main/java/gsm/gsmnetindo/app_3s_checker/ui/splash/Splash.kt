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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import gsm.gsmnetindo.app_3s_checker.BuildConfig
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.internal.ScopedActivity
import gsm.gsmnetindo.app_3s_checker.ui.Intro.IntroActivity
import gsm.gsmnetindo.app_3s_checker.ui.login.loginverification
import gsm.gsmnetindo.app_3s_checker.ui.main.MainActivity
import gsm.gsmnetindo.app_3s_checker.ui.main.result.ResultActivity
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModel
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModelFactory
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import retrofit2.HttpException
import java.net.SocketTimeoutException

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
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Koneksi Internet anda buruk, harap pastikan koneksi internet anda baik Dan jalankan ulang aplikasi 3s checker")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton("IYA") { dialog, which ->
            Toast.makeText(applicationContext,
                "ya", Toast.LENGTH_SHORT).show()
            finish()
            System.exit(0)
        }
        builder.show()

    }

    private fun checkUpdate() = launch {
        try {
            splashViewModel.getLatestVersion().observe(this@Splash, Observer {
                val currentVersion = BuildConfig.VERSION_CODE
                if (currentVersion < it.appVersionCode) {
                    AlertDialog.Builder(this@Splash).apply {
                        setTitle("Versi terbaru telah rilis")
                        setMessage("Mohon update ${it.appName} ke versi ${it.appVersionName}")
                        setPositiveButton("Ok") { dialog, _ ->
                            dialog.dismiss()
                            finish()
//                        Intent(Intent(this@SplashActivity, Intent.ACTION_ANSWER))
                        }
                        show()
                    }
                } else {
                    isFirstTime()
                }
            })
        } catch (e: Exception) {
            Log.e("version update", e.message, e)
            when(e) {
                is SocketTimeoutException -> {
                    Toast.makeText(this@Splash, "Tidak bisa menyambung ke server, coba beberapa saat lagi", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(this@Splash, e.message, Toast.LENGTH_LONG).show()
                }
            }
            finish()
        }
    }
    private fun isFirstTime() {
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
        Intent(this, loginverification::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(this)
            finish()
        }
    }
}