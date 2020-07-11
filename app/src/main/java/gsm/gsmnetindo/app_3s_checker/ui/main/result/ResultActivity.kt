package gsm.gsmnetindo.app_3s_checker.ui.main.result

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.data.network.response.barcode.*
import gsm.gsmnetindo.app_3s_checker.internal.ScopedActivity
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class ResultActivity: ScopedActivity(), KodeinAware {
    override val kodein by closestKodein()
    private val resultViewModelFactory by instance<ResultViewModelFactory>()
    private lateinit var resultViewModel: ResultViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        resultViewModel = ViewModelProvider(this, resultViewModelFactory).get(ResultViewModel::class.java)
        bindViewModel()

        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = ResultPagerAdapter(this, supportFragmentManager)
        val tabs: TabLayout = findViewById(R.id.tabs)

        tabs.setupWithViewPager(viewPager)
        tabs.setSelectedTabIndicatorColor(Color.WHITE)
        tabs.setTabTextColors(Color.parseColor("#eeeeee"), Color.WHITE)
    }
    private fun bindViewModel() = launch {
        val account = Account(
            "asd",
            "2020-02-22T12:12:12Z[UTC]",
            "dio@mail.com",
            "6281249499076",
            7,
            "active",
            1,
            "2020-02-22T12:12:12Z[UTC]",
            "6281249499076"
        )
        val history = listOf<History>(
            History("tidak", "tidak", "tidak", "tidak", 1, "2020-02-22T12:12:12Z[UTC]", 1, "2020-02-22T12:12:12Z[UTC]", true),
            History("ya", "tidak", "tidak", "tidak", 1, "2020-02-22T12:12:12Z[UTC]", 1, "2020-02-22T12:12:12Z[UTC]", true)
        )
        val status = Status(
            1,
            "2020-02-22T12:12:12Z[UTC]",
            "negative",
            "2020-02-22T12:12:12Z[UTC]",
            true
        )
        val user = User(
            "1997-06-01",
            "Banyuwangi",
            "2020-02-22T12:12:12Z[UTC]",
            "male",
            "Dio Lantief Egalitarian",
            "2020-02-22T12:12:12Z[UTC]"
        )
//        resultViewModel.setBarcode("xJ6UMTh3lokKiHvPI78my2ZprcNf5tV9")
//        resultViewModel.setDetail(barcodeDetailResponse)
    }

    override fun onDestroy() {
        super.onDestroy()
        resultViewModel.clear()
    }
}