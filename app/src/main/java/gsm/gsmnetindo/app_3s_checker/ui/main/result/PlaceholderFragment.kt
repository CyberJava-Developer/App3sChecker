package gsm.gsmnetindo.app_3s_checker.ui.main.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import gsm.gsmnetindo.app_3s_checker.R

class PlaceholderFragment: Fragment() {

    private lateinit var resultViewModel: ResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultViewModel = ViewModelProvider(this).get(ResultViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.fragment_result, container, false)
//        val textView: TextView = root.findViewById(R.id.section_label)
        resultViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
        })
        var viewLayout: LayoutInflater
        resultViewModel.layout.observe(viewLifecycleOwner, Observer {
            root = inflater.inflate(it, container, false)
//            textView.text = "${textView.text} with id $it"
//            view = inflater.inflate(it, container, true)
//            view.conten
//            view.id = it
        })
        return root
    }
    companion object {
        private const val ARG_SECTION = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION, sectionNumber)
                }
            }
        }
    }
}