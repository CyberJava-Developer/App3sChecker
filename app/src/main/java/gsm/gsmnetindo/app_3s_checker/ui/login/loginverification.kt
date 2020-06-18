package gsm.gsmnetindo.app_3s_checker.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import gsm.gsmnetindo.app_3s_checker.R
import kotlinx.android.synthetic.main.activity_login.*

class loginverification: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        next_login.setOnClickListener{
            val intent = Intent(this, verivicationclass::class.java)
            startActivity(intent)
            finish()
        }
    }
}