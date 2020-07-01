package gsm.gsmnetindo.app_3s_checker.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.ui.Intro.IntroActivity

class Splash : AppCompatActivity() {
    lateinit var topanim:Animation
    lateinit var buttomanim:Animation
    lateinit var image:ImageView
    lateinit var logotext:ImageView
    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        topanim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        buttomanim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)
        image = findViewById(R.id.Logo)
        logotext = findViewById(R.id.textlogo)

        image.animation = topanim
        logotext.animation = buttomanim

        handler = Handler()
        handler.postDelayed({

            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
            finish()
        }, 5000)
    }
}