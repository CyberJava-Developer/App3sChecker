package gsm.gsmnetindo.app_3s_checker.ui.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import gsm.gsmnetindo.app_3s_checker.R

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
        val builder = android.app.AlertDialog.Builder(context)
        builder.setTitle("Pesan")
        builder.setMessage("Setelah menggunakan menu perpesanan whatsapp, silakan klik tombol kembali agar anda bisa kembali ke aplikasi s3 checker")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton("IYA") { dialog, which ->
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/628155555830"))
            startActivity(i)
            //finish()
        }
        builder.show()
    }
}