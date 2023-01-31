package com.angiuprojects.gamecatalog.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.entities.implementation.Season

class AddSeasonRecyclerAdapter (private val dataSet : MutableList<Season>)
    : RecyclerView.Adapter<AddSeasonRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView
        var episodes: TextView

        init {
            name = view.findViewById(R.id.name)
            episodes = view.findViewById(R.id.episodes)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.add_season_view, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = buildString {
            append("Stagione ")
            append(dataSet[holder.adapterPosition].number)
        }

        holder.episodes.text = buildString {
            append(dataSet[holder.adapterPosition].seenEpisodes)
            append("/")
            append(dataSet[holder.adapterPosition].totalEpisodes)
        }
    }

    override fun getItemCount() = dataSet.size
}