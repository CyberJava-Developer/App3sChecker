package gsm.gsmnetindo.app_3s_checker.ui.dashboard.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import gsm.gsmnetindo.app_3s_checker.R
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
        showCovidData()
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
        } catch (e: HttpException){
            Toast.makeText(requireContext(), e.message(), Toast.LENGTH_LONG).show()
        }
    }
}