package com.angiuprojects.gamecatalog.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.entities.implementation.Episode

class EpisodeRecyclerAdapter(private val dataSet : MutableList<Episode>, private val context: Context) : RecyclerView.Adapter<EpisodeRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView
        var checkBox: CheckBox
        var layout: RelativeLayout

        init {
            name = view.findViewById(R.id.name)
            checkBox = view.findViewById(R.id.check)
            layout = view.findViewById(R.id.episode_view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.season_view, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = dataSet[holder.adapterPosition].name
        holder.checkBox.isChecked = dataSet[holder.adapterPosition].seenOrRead
    }

    override fun getItemCount() = dataSet.size
}