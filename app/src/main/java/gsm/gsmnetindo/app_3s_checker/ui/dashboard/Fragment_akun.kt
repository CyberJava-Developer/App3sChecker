package gsm.gsmnetindo.app_3s_checker.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.internal.ScopedFragment
import gsm.gsmnetindo.app_3s_checker.ui.login.loginverification
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModel
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModelFactory
import kotlinx.android.synthetic.main.activity_akun.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class Fragment_akun : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val accountViewModelFactory: AccountViewModelFactory by instance()
    private lateinit var accountViewModel: AccountViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_akun, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progressktp.visibility = View.GONE
        accountViewModel = ViewModelProvider(this, accountViewModelFactory).get(AccountViewModel::class.java)
        account_button_logout.setOnClickListener {
            logout()
        }
        bindUI()
        syncDetail()
    }
    private fun logout(){
        accountViewModel.logout()
        if (activity != null){
            Intent(activity, loginverification::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(this)
                requireActivity().finish()
            }
        }
    }
    private fun bindUI() {
        accountViewModel.detail.observe(viewLifecycleOwner, Observer {
            Log.i("fragment detail", "$it")
            txtname.text = it.detail.name
            nomortelepon.text = "+${it.account.phone}"
            txtlahir.text = "${it.detail.bornPlace}, ${it.detail.bornDate}"

        })
    }
    private fun syncDetail() = launch {
        try {
            accountViewModel.syncDetail()
        } catch (e: Exception) {
            Log.e("fetchDetail", e.message, e)
        }
    }
}