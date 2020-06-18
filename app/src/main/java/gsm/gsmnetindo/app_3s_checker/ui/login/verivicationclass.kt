package gsm.gsmnetindo.app_3s_checker.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import gsm.gsmnetindo.app_3s_checker.MainActivity
import gsm.gsmnetindo.app_3s_checker.R
import kotlinx.android.synthetic.main.activity_verification.*

class verivicationclass : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)


        btn_verify.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}