package gsm.gsmnetindo.app_3s_checker.ui.main.result.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.data.network.response.barcode.Location
import gsm.gsmnetindo.app_3s_checker.internal.ScopedFragment
import gsm.gsmnetindo.app_3s_checker.ui.main.result.ResultViewModel
import gsm.gsmnetindo.app_3s_checker.ui.main.result.ResultViewModelFactory
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModel
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModelFactory
import kotlinx.android.synthetic.main.fragment_result_location.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class LocationFragment: ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val resultViewModelFactory by instance<ResultViewModelFactory>()
    private val accountViewModelFactory by instance<AccountViewModelFactory>()
    private lateinit var resultViewModel: ResultViewModel
    private lateinit var accountViewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        resultViewModel = ViewModelProvider(this, resultViewModelFactory).get(ResultViewModel::class.java)
        return inflater.inflate(R.layout.fragment_result_location, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        resultViewModel.details.observe(viewLifecycleOwner, Observer {
            initRecyclerView(it.location)
        })
    }
    private fun initRecyclerView(items: List<Location>){
        val groupAdapter = GroupAdapter<ViewHolder>()
        location_recyclerview.adapter = groupAdapter
        location_recyclerview.layoutManager = LinearLayoutManager(requireContext())
        items.map {
            groupAdapter.add(LocationItem(it))
        }
    }
}