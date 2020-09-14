package gsm.gsmnetindo.app_3s_checker.ui.dashboard

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.internal.ScopedFragment
import gsm.gsmnetindo.app_3s_checker.internal.Secret
import gsm.gsmnetindo.app_3s_checker.internal.glide.GlideApp
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
        accountViewModel =
            ViewModelProvider(this, accountViewModelFactory).get(AccountViewModel::class.java)
        account_button_logout.setOnClickListener {
            logout()
        }
        bindUI()
        syncDetail()
    }

    private fun logout() {
        accountViewModel.logout()
        if (activity != null) {
            Intent(activity, loginverification::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(this)
                requireActivity().finish()
            }
        }
    }

    private fun bindUI() {
        accountViewModel.detail.observe(viewLifecycleOwner, {
            Log.i("fragment detail", "$it")
            txtname.text = it.detail.name
            nomortelepon.text = "+${it.account.phone}"
            txtlahir.text = "${it.detail.bornPlace}, ${it.detail.bornDate}"
            loadAvatar(ObjectKey(it.account.avatar))
        })
    }

    private fun syncDetail() = launch {
        try {
            accountViewModel.syncDetail()
        } catch (e: Exception) {
            Log.e("fetchDetail", e.message, e)
        }
    }

    private fun loadAvatar(key: ObjectKey) {
        val url =
            "${Secret.baseApi()}${Secret.apiVersion()}/${accountViewModel.getPhonePref()}/avatar.jpg"
        val glideUrl = GlideUrl(
            url,
            LazyHeaders.Builder()
                .addHeader("Api-Key", Secret.apiKey())
                .addHeader("Authorization", "Bearer ${accountViewModel.getToken().value}")
                .build()
        )
        GlideApp.with(this)
            .load(glideUrl)
            .signature(key)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return e != null
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    target.apply { account_avatar.setImageDrawable(resource) }
                    return resource != null
                }

            })
            .into(account_avatar)
    }
}