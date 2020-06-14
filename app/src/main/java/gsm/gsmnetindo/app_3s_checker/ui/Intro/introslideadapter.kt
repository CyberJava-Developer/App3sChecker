package gsm.gsmnetindo.app_3s_checker.ui.Intro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import gsm.gsmnetindo.app_3s_checker.R
import kotlinx.android.synthetic.main.slide_item_container.*

class introslideadapter(private val introslide: List<dataintroslide>):
    RecyclerView.Adapter<introslideadapter.Introslideholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Introslideholder {
        return Introslideholder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.slide_item_container, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return  introslide.size
    }

    override fun onBindViewHolder(holder: Introslideholder, position: Int) {
        holder.bind(introslide[position])
    }

    inner class Introslideholder(view: View):RecyclerView.ViewHolder(view){

        private val  texttitle = view.findViewById<TextView>(R.id.TextTitle)
        private val  textdeskription = view.findViewById<TextView>(R.id.TextDeskription)
        private val  imageicon = view.findViewById<ImageView>(R.id.imageslideicon)

        fun bind(introslide: dataintroslide){
            texttitle.text = introslide.title
            textdeskription.text = introslide.deskripsi
            imageicon.setImageResource(introslide.icon)
        }
    }
}