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
import com.angiuprojects.gamecatalog.entities.implementation.Season
import com.angiuprojects.gamecatalog.entities.implementation.TVShow
import com.angiuprojects.gamecatalog.utilities.Constants
import com.angiuprojects.gamecatalog.utilities.Utils

class TVShowRecyclerAdapter(private val dataSet : MutableList<TVShow>, private val context: Context) : RecyclerView.Adapter<TVShowRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView
        var expand: ImageButton
        var seasonsCompleted: TextView
        var seasons: RecyclerView
        var layout: RelativeLayout

        init {
            name = view.findViewById(R.id.name)
            expand = view.findViewById(R.id.arrow)
            seasonsCompleted = view.findViewById(R.id.seasons_completed)
            seasons = view.findViewById(R.id.seasons_recycler_view)
            layout = view.findViewById(R.id.tv_show_view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tv_show_view, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = dataSet[holder.adapterPosition].name
        holder.expand.setOnClickListener{Utils.getInstance().onClickExpandCollapse(holder.seasons, holder.expand)}
        holder.seasonsCompleted.text = populateCompletedSeasons(dataSet[holder.adapterPosition].volumes)

        holder.seasons.visibility = View.GONE
        setRecyclerAdapter(dataSet[holder.adapterPosition].volumes, context, holder.seasons)
    }

    private fun populateCompletedSeasons(seasonList: MutableList<Season>) : String {
        var completedSeasons = 0

        try {
            seasonList.forEach{
                if(it.completed) completedSeasons++
            }
        } catch (e: Exception) {
            Log.e(Constants.getInstance().logger, "La lista delle stagioni è vuota o nulla. ${e.message}")
            return "0/0"
        }

        return "$completedSeasons/${seasonList.size}"
    }

    private fun setRecyclerAdapter(seasonList: MutableList<Season>, context: Context, recyclerView: RecyclerView) {

        val adapter = SeasonRecyclerAdapter(seasonList, context)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        //swipeToDelete(cardList, adapter)
    }

    override fun getItemCount() = dataSet.size
}