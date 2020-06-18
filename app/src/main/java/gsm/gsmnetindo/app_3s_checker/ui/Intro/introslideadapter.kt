package gsm.gsmnetindo.app_3s_checker.ui.Intro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import gsm.gsmnetindo.app_3s_checker.R

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

        private val  imageicon = view.findViewById<ImageView>(R.id.imageslideicon)

        fun bind(introslide: dataintroslide){
            imageicon.setImageResource(introslide.icon)
        }
    }
}