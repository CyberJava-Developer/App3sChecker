package gsm.gsmnetindo.app_3s_checker.ui.main.result

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import gsm.gsmnetindo.app_3s_checker.ui.main.result.detail.DetailFragment
import gsm.gsmnetindo.app_3s_checker.ui.main.result.location.LocationFragment
import gsm.gsmnetindo.app_3s_checker.ui.main.result.questionnaire.QuestionnaireFragment
import gsm.gsmnetindo.app_3s_checker.ui.viewmodel.AccountViewModel

private val TAB_TITLES = arrayOf(
    "Detail"
)
private lateinit var accountViewModel: AccountViewModel
class ResultPagerAdapterkeamanan(
    private val context: Context,
    fm: FragmentManager
): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val pageskeamanan = listOf(
        DetailFragment()
    )

    override fun getItem(position: Int): Fragment {
        return pageskeamanan[position] as Fragment
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return TAB_TITLES[position]
    }
}