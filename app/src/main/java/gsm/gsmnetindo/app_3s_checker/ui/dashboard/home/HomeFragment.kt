package gsm.gsmnetindo.app_3s_checker.ui.dashboard.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.data.db.entity.FeedItem
import gsm.gsmnetindo.app_3s_checker.internal.NoConnectivityException
import gsm.gsmnetindo.app_3s_checker.internal.ScopedFragment
import gsm.gsmnetindo.app_3s_checker.smsgateway.networkchecker.isNetworkAvailable
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.net.SocketTimeoutException
import kotlin.system.exitProcess

class HomeFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val homeViewModelFactory: HomeViewModelFactory by instance()
    private lateinit var homeViewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)
        if(!isNetworkAvailable.isNetwork(requireContext().applicationContext)) {
            alern()
        }
        return inflater.inflate(R.layout.activity_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        feed_refresh.setProgressBackgroundColorSchemeResource(R.color.colorPrimary)
        feed_refresh.setColorSchemeColors(Color.WHITE)
        feed_refresh.isRefreshing = true
        feed_refresh.setOnRefreshListener {
            showCovidData()
            showFeeds()
        }
        showCovidData()
        showFeeds()
    }
    private fun showCovidData() = launch {
        try {
            homeViewModel.covidData().observe(viewLifecycleOwner, Observer {
                if (it.isNullOrEmpty()) return@Observer
                else {
                    val thisDay = it.last()
                    textView_data_confirmed.text = "${thisDay.confirmed}"
                    textView_data_recovered.text = "${thisDay.recovered}"
                    textView_data_deaths.text = "${thisDay.deaths}"
                }
            })
        } catch (e: Exception){
            when (e) {
                is SocketTimeoutException -> {
                    Log.d("Internet", "Internet Tidak Tersedia")
                    Toast.makeText(context, "Internet Tidak Tersedia", Toast.LENGTH_SHORT).show()
                }
                is IllegalStateException -> {
                    Log.d("Internet", "Internet Tidak Tersedia")
                }
                else -> {
                    Log.d("Internet", "Internet Tidak Tersedia")
                }
            }
        }
    }
    private fun showFeeds() = launch {
        try {
            homeViewModel.feeds().observe(viewLifecycleOwner, Observer {
                if (it.isNullOrEmpty()) return@Observer
                else {
                    feed_refresh.isRefreshing = false
                    initRecyclerView(it.reversed())
                }
            })
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException -> {
                    Log.d("Internet", "Internet Tidak Tersedia")
                    Toast.makeText(context, "Internet Tidak Tersedia", Toast.LENGTH_SHORT).show()
                }
                is IllegalStateException -> {
                    Log.d("Internet", "Internet Tidak Tersedia")
                }
                else -> {
                    Log.d("Internet", "Internet Tidak Tersedia")
                }
            }
        }
    }

    private fun alern() {
        SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Kesalahan Jaringan")
            .setContentText("internet koneksi buruk, pastikan koneksi anda stabil dan silakan coba kembali")
            .setConfirmText("Terima Kasih")
            .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation()
                exitProcess(0)
            }
            .show()
    }

    private fun initRecyclerView(items: List<FeedItem>){
        val groupAdapter = GroupAdapter<ViewHolder>()
        feed_recyclerView.adapter = groupAdapter
        feed_recyclerView.layoutManager = LinearLayoutManager(requireContext())
        items.map {
            groupAdapter.add(FeedCardItem(requireContext(), it))
        }
    }
}