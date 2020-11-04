package gsm.gsmnetindo.app_3s_checker.ui.main.result

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

private val TAB_TITLES = arrayOf(
    "Detail", "Check Up", "Locations"
)

class ResultPagerAdapter(
    private val context: Context,
    private val pages: List<Page>,
    fm: FragmentManager,
): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
//    private val pages = listOf(
//        DetailFragment(),
//        QuestionnaireFragment(),
//        LocationFragment()
//    )

    override fun getItem(position: Int): Fragment {
        return pages[position].fragment
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return pages[position].title
    }
}