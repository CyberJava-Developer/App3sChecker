package gsm.gsmnetindo.app_3s_checker.ui.Intro

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import gsm.gsmnetindo.app_3s_checker.ui.Intro.intro3.fragment_intro3
import gsm.gsmnetindo.app_3s_checker.ui.Intro.masker.fragment_masker
import gsm.gsmnetindo.app_3s_checker.ui.Intro.tes2.fragment_tes2

class introslideadapter(fm : FragmentManager): FragmentPagerAdapter(fm){

    public val pages = listOf(
        fragment_masker(),
        fragment_tes2(),
        fragment_intro3()
    )
    // menentukan fragment yang akan dibuka pada posisi tertentu
    override fun getCount(): Int {
        return pages.size
    }

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }
    // judul untuk tabs
    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "First Tab"
            1 -> "Second Tab"
            else -> "Third tab"
        }
    }
}