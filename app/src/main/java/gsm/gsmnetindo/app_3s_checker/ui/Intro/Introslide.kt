package gsm.gsmnetindo.app_3s_checker.ui.Intro

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.ui.login.loginverification
import kotlinx.android.synthetic.main.activity_introslide.*

class Introslide : AppCompatActivity() {

    private  val Introslideadapter = introslideadapter(
        listOf(
            dataintroslide(
                R.drawable.msker
            ),
            dataintroslide(
              R.drawable.test
            ),
            dataintroslide(
             R.drawable.pmeriksaan
            )
        )
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_introslide)
        val connectifitymanager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activenetwork: NetworkInfo? = connectifitymanager.activeNetworkInfo
        val isconnec: Boolean = activenetwork?.isConnectedOrConnecting ==true
        if (!isconnec){
            Toast.makeText(applicationContext,
                "Koneksi Internet Tidak ada", Toast.LENGTH_SHORT).show()

        }
        else{
            val txt = findViewById<TextView>(R.id.txtskip)
            txt.setOnClickListener{
                if (isconnec){
                    val intent = Intent(this, loginverification::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(applicationContext,
                        "Koneksi Internet Tidak ada", Toast.LENGTH_SHORT).show()
                }
//                val intent = Intent(this, loginverification::class.java)
//                startActivity(intent)
//                finish()
            }
        }

        introsliderviewpage.adapter = Introslideadapter
        setupIndicator()
        setcurrentIndicator(0)

        introsliderviewpage.registerOnPageChangeCallback(object :
        ViewPager2.OnPageChangeCallback(){

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setcurrentIndicator(position)
            }
        })
    }

    private fun setupIndicator(){
        val indicator = arrayOfNulls<ImageView>(Introslideadapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8, 0,8,0)
        for (i in indicator.indices){
            indicator[i] = ImageView(applicationContext)
            indicator[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_activated
                    )
                )
                this?.layoutParams = layoutParams
            }
            indicatorcontainer.addView(indicator[i])
        }
    }

    private fun setcurrentIndicator(index: Int){
        val childCount = indicatorcontainer.childCount
        for (i in 0 until childCount){
            val imageView = indicatorcontainer[i] as ImageView

            if (i == index){
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
            } else{
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_activated
                    )
                )
            }
        }
    }
}