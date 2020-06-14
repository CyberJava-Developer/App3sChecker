package gsm.gsmnetindo.app_3s_checker.ui.Intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import gsm.gsmnetindo.app_3s_checker.MainActivity
import gsm.gsmnetindo.app_3s_checker.R
import kotlinx.android.synthetic.main.activity_introslide.*

class Introslide : AppCompatActivity() {

    private  val Introslideadapter = introslideadapter(
        listOf(
            dataintroslide(
                "Menggunakan Masker",
                "Pastikan Menggunakan Masker Sesuai Protokol Kesehatan",
                R.drawable.s2
            ),
            dataintroslide(
                "Jaga Jarak",
            "Pengaturan Jarak Dapat Mencegah Penyebaran COVID - 19",
                R.drawable.s1
            ),
            dataintroslide(
                "Cegah Isu Negative",
            "Pastikan Semua Kondisi Berdasarkan Data",
                R.drawable.s3
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introslide)
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

        buttonnext.setOnClickListener{
            if (introsliderviewpage.currentItem + 1 < Introslideadapter.itemCount){
                introsliderviewpage.currentItem+=1
            } else{
                Intent(applicationContext, MainActivity::class.java).also{
                    startActivity(it)
                }
            }
        }

        val txt = findViewById<TextView>(R.id.txtskip)
        txt.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
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