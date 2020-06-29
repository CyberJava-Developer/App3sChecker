package gsm.gsmnetindo.app_3s_checker.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import gsm.gsmnetindo.app_3s_checker.R
import kotlinx.android.synthetic.main.activity_verification.*


class verivicationclass : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_verification)

        //visible layout
        progressBaricon.visibility = View.VISIBLE
        frameLayout.visibility = View.GONE
        frameLayout2.visibility = View.GONE

        Handler(Looper.getMainLooper()).postDelayed({
            frameLayout.visibility = View.VISIBLE
            frameLayout2.visibility = View.VISIBLE
            progressBaricon.visibility = View.GONE
        }, 2000)

        val myIntent = intent
        if (myIntent.hasExtra("number")) {
            val mText = findViewById<TextView>(R.id.txt_numberphone)
            mText.text = myIntent.getStringExtra("number")
        }
        btn_verify.setOnClickListener {
            val intent = Intent(this, loginverification::class.java)
            startActivity(intent)
            finish()
        }

        Ubah_noponsel.setOnClickListener {
            startActivity(Intent(this, loginverification::class.java))
            finish()
        }

        Contactperson.setOnClickListener {
            Contactperson.setText(R.string.link1)
            Contactperson.movementMethod = LinkMovementMethod.getInstance()
        }
    }
}