package com.angiuprojects.gamecatalog.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.entities.implementation.Episode
import com.angiuprojects.gamecatalog.entities.implementation.Season
import com.angiuprojects.gamecatalog.utilities.Constants
import com.angiuprojects.gamecatalog.utilities.Utils

class SeasonRecyclerAdapter(private val dataSet : MutableList<Season>, private val context: Context) : RecyclerView.Adapter<SeasonRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView
        var expand: ImageButton
        var episodesCompleted: TextView
        var episodes: RecyclerView
        var layout: RelativeLayout

        init {
            name = view.findViewById(R.id.name)
            expand = view.findViewById(R.id.arrow)
            episodesCompleted = view.findViewById(R.id.episodes_completed)
            episodes = view.findViewById(R.id.episodes_recycler_view)
            layout = view.findViewById(R.id.season_view)
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
        holder.name.text = dataSet[holder.adapterPosition].name
        holder.expand.setOnClickListener{ Utils.getInstance().onClickExpandCollapse(holder.episodes, holder.expand)}
        holder.episodesCompleted.text = populateCompletedEspisodes(dataSet[holder.adapterPosition].episodes)

        holder.episodes.visibility = View.GONE
        setRecyclerAdapter(dataSet[holder.adapterPosition].episodes, context, holder.episodes)
    }

    private fun populateCompletedEspisodes(episodeList: MutableList<Episode>) : String {
        var completedEpisodes = 0

        try {
            episodeList.forEach{
                if(it.seenOrRead) completedEpisodes++
            }
        } catch (e: Exception) {
            Log.e(Constants.getInstance().logger, "La lista degli episodi è vuota o nulla. ${e.message}")
            return "0/0"
        }

        return "$completedEpisodes/${episodeList.size}"
    }

    private fun setRecyclerAdapter(episodeList: MutableList<Episode>, context: Context, recyclerView: RecyclerView) {

        val adapter = EpisodeRecyclerAdapter(episodeList, context)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        //swipeToDelete(cardList, adapter)
    }


    override fun getItemCount() = dataSet.size
}