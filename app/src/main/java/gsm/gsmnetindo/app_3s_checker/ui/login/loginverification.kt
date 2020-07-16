package gsm.gsmnetindo.app_3s_checker.ui.login

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import gsm.gsmnetindo.app_3s_checker.R
import kotlinx.android.synthetic.main.activity_login.*


class loginverification: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)

        next_login.setOnClickListener{
            val msg: String = phone_login.text.toString()

            //check if the EditText have values or not
            if(msg.trim().length>0) {
                Toast.makeText(applicationContext, "Tunggu sebentar hingga anda mendapatkan 4 digit nomor verifikasi ", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, verificationlogin::class.java)
                val mEditText = findViewById<EditText>(R.id.phone_login)
                val str = mEditText.text.toString()
                intent.putExtra("number", str)
                startActivity(intent)
                finish()

            }else{
                alerndialog()
            }
        }
    }

    fun alerndialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Mohon Isi Nomor Ponsel Dengan Benar")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton("IYA") { dialog, which ->
//            Toast.makeText(applicationContext,
//                "ya", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("TIDAK") { dialog, which ->
//            Toast.makeText(applicationContext,
//                "tidak", Toast.LENGTH_SHORT).show()
        }
        builder.show()

    }


}