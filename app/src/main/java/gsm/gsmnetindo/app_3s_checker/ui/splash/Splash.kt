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
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import retrofit2.HttpException

class Splash : ScopedActivity(), KodeinAware {

    override val kodein by closestKodein()
    private val splashViewModelFactory: SplashViewModelFactory by instance()
    private lateinit var splashViewModel: SplashViewModel

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
            Toast.makeText(this,"tidak ada koneksi internet", Toast.LENGTH_LONG).show();
            finish()
        } else {
            checkUpdate()
        }
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
        } catch (e: HttpException) {
            Log.e("version update", e.message(), e)
            Toast.makeText(this@Splash, e.message(), Toast.LENGTH_LONG).show()
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

    }
}