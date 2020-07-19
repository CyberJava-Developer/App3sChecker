package gsm.gsmnetindo.app_3s_checker.ui.dashboard.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.data.db.entity.FeedItem
import gsm.gsmnetindo.app_3s_checker.internal.ScopedFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import retrofit2.HttpException

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
            when(e){
                is IllegalStateException -> {

                }
                else -> {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
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
                    initRecyclerView(it)
                }
            })
        } catch (e: Exception){
            when(e){
                is IllegalStateException -> {

                }
                else -> {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                    feed_refresh.isRefreshing = false
                }
            }
        }
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