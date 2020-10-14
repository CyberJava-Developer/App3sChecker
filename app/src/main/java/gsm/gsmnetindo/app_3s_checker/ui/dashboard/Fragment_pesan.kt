package gsm.gsmnetindo.app_3s_checker.ui.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import gsm.gsmnetindo.app_3s_checker.R
import kotlin.system.exitProcess

class Fragment_pesan : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        alern()
        return inflater.inflate(R.layout.activity_pesan, container, false)
    }

    fun alern(){
        SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Pesan")
            .setContentText("Setelah menggunakan menu perpesanan whatsapp, silakan klik tombol kembali agar anda bisa kembali ke aplikasi s3 checker")
            .setConfirmText("Terima Kasih")
            .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation()
                val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/628155555830"))
                startActivity(i)
            }
            .show()
    }
}