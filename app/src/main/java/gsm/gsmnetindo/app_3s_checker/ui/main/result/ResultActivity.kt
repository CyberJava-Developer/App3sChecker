package gsm.gsmnetindo.app_3s_checker.ui.main.result

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.ogaclejapan.smarttablayout.SmartTabLayout
import gsm.gsmnetindo.app_3s_checker.R
import gsm.gsmnetindo.app_3s_checker.internal.ScopedActivity
import gsm.gsmnetindo.app_3s_checker.ui.main.result.detail.DetailFragment
import gsm.gsmnetindo.app_3s_checker.ui.main.result.location.LocationFragment
import gsm.gsmnetindo.app_3s_checker.ui.main.result.questionnaire.QuestionnaireFragment
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModel
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModelFactory
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class ResultActivity : ScopedActivity(), KodeinAware {
    override val kodein by closestKodein()
    private val resultViewModelFactory by instance<ResultViewModelFactory>()
    private lateinit var resultViewModel: ResultViewModel
    private lateinit var accountViewModel: AccountViewModel
    private val accountViewModelFactory: AccountViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_result)

        resultViewModel =
            ViewModelProvider(this, resultViewModelFactory).get(ResultViewModel::class.java)
        bindViewModel()
        accountViewModel =
            ViewModelProvider(this, accountViewModelFactory).get(AccountViewModel::class.java)

        val viewPager: ViewPager = findViewById(R.id.view_pager)

        val pages = when (accountViewModel.getRolePref()) {
            2 -> listOf(
                Page("Detail", DetailFragment())
            )

            3 -> listOf(
                Page("Detail", DetailFragment()),
                Page("Check Up", QuestionnaireFragment()),
            )

            4, 5, 6, 7 -> listOf(
                Page("Detail", DetailFragment()),
                Page("Check Up", QuestionnaireFragment()),
                Page("Location", LocationFragment())
            )

            else -> listOf(
                Page("Detail", DetailFragment())
            )
        }
        viewPager.adapter = ResultPagerAdapter(pages, supportFragmentManager)
//        when(accountViewModel.getRolePref()) {
//            2 -> { viewPager.adapter = ResultPagerAdapterkeamanan(this, supportFragmentManager) }
//            3 -> { viewPager.adapter = ResultPagerAdapterparamedis(this, supportFragmentManager) }
//            4,5 or 7-> { viewPager.adapter = ResultPagerAdapter(this, supportFragmentManager) }
//            else -> { navbar.inflateMenu(R.menu.dashboard_menu_role_else) }
//        }

        val tabs: SmartTabLayout = findViewById(R.id.tabs)

        tabs.setViewPager(viewPager)

    }

    private fun bindViewModel() = launch {
//        resultViewModel.setBarcode("xJ6UMTh3lokKiHvPI78my2ZprcNf5tV9")
//        resultViewModel.setDetail(barcodeDetailResponse)
    }

    override fun onDestroy() {
        super.onDestroy()
        resultViewModel.clear()
    }
    companion object {
        const val INTENT_EXTRA_NAME = "code"
    }
}