package com.angiuprojects.gamecatalog.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.entities.implementation.Saga

class AddSagaRecyclerAdapter (private val dataSet : MutableList<Saga>)
    : RecyclerView.Adapter<AddSeasonRecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddSeasonRecyclerAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.add_season_view, parent, false)

        return AddSeasonRecyclerAdapter.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddSeasonRecyclerAdapter.MyViewHolder, position: Int) {
        holder.name.text = dataSet[holder.adapterPosition].name

        holder.episodes.text = buildString {
            append(getCompletedSeasons(holder))
            append("/")
            append(dataSet[holder.adapterPosition].seasons.size)
        }
    }

    private fun getCompletedSeasons(holder: AddSeasonRecyclerAdapter.MyViewHolder) : Int {
        var completedSeasons = 0
        dataSet[holder.adapterPosition].seasons.forEach { if(it.isCompleted(it)) completedSeasons++}
        return completedSeasons
    }

    override fun getItemCount() = dataSet.size
}