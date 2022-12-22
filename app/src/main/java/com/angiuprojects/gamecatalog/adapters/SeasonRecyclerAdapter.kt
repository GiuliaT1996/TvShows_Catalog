package com.angiuprojects.gamecatalog.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.entities.implementation.Season
import com.angiuprojects.gamecatalog.entities.implementation.TVShow
import com.angiuprojects.gamecatalog.queries.Queries
import com.angiuprojects.gamecatalog.utilities.Constants

class SeasonRecyclerAdapter(private val dataSet : MutableList<Season>,
                            private val parent: TVShow,
                            private val parentRecyclerAdapter: TVShowRecyclerAdapter,
                            private val position: Int,
                            private val parentViewHolder: TVShowRecyclerAdapter.MyViewHolder)
    : RecyclerView.Adapter<SeasonRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView
        var episodesCompleted: TextView
        var plusButton: ImageButton
        var minusButton: ImageButton
        var progressBar: ProgressBar

        init {
            name = view.findViewById(R.id.name)
            episodesCompleted = view.findViewById(R.id.episodes_completed)
            plusButton = view.findViewById(R.id.plus_button)
            minusButton = view.findViewById(R.id.minus_button)
            progressBar = view.findViewById(R.id.progress_bar)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.season_view, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val season = dataSet[holder.adapterPosition]

        holder.name.text = season.name
        setProgressBar(holder, dataSet[holder.adapterPosition])
                holder.plusButton.setOnClickListener{plus(holder)}
        holder.minusButton.setOnClickListener{minus(holder)}
    }

    private fun plus(holder: MyViewHolder) {
        if(dataSet[holder.adapterPosition].seenEpisodes == dataSet[holder.adapterPosition].totalEpisodes) {
            Log.i(Constants.getInstance().logger, "Non è più possibile aggiungere episodi visti")
            return
        }
        dataSet[holder.adapterPosition].seenEpisodes++
        setProgressBar(holder, dataSet[holder.adapterPosition])
        Queries.getInstance().addUpdate(Constants.getInstance().tvShowDbReference, parent)

        if(dataSet[holder.adapterPosition].seenEpisodes == dataSet[holder.adapterPosition].totalEpisodes)
            updateParent(parentViewHolder)
    }

    private fun minus(holder: MyViewHolder) {
        if(dataSet[holder.adapterPosition].seenEpisodes == 0) {
            Log.i(Constants.getInstance().logger, "Non è più possibile rimuovere episodi visti")
            return
        }

        dataSet[holder.adapterPosition].seenEpisodes--
        setProgressBar(holder, dataSet[holder.adapterPosition])
        Queries.getInstance().addUpdate(Constants.getInstance().tvShowDbReference, parent)

        if(dataSet[holder.adapterPosition].seenEpisodes == dataSet[holder.adapterPosition].totalEpisodes - 1)
            updateParent(parentViewHolder)
    }

    private fun updateParent(holder: TVShowRecyclerAdapter.MyViewHolder){
        parentRecyclerAdapter.updateCompletedSeasons(position = position, holder)
    }

    private fun setProgressBar(holder: MyViewHolder, season: Season) {
        holder.episodesCompleted.text = buildString {
            append(season.seenEpisodes)
            append("/")
            append(season.totalEpisodes)
        }

        holder.progressBar.progress = (season.seenEpisodes * 100) / season.totalEpisodes
    }

    override fun getItemCount() = dataSet.size
}