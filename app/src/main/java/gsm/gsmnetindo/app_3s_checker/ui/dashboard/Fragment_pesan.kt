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
        val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/628155555830"))
        startActivity(i)
        return inflater.inflate(R.layout.activity_pesan, container, false)
    }
}