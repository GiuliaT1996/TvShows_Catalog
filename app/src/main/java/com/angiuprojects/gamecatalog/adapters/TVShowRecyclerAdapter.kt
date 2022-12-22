package com.angiuprojects.gamecatalog.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
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
        var completedImage: ImageView
        var layout: RelativeLayout

        init {
            name = view.findViewById(R.id.name)
            expand = view.findViewById(R.id.arrow)
            seasonsCompleted = view.findViewById(R.id.seasons_completed)
            seasons = view.findViewById(R.id.seasons_recycler_view)
            completedImage = view.findViewById(R.id.completed)
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
        holder.seasonsCompleted.text = populateCompletedSeasons(dataSet[holder.adapterPosition].volumes, holder)

        holder.seasons.visibility = View.GONE
        setRecyclerAdapter(dataSet[holder.adapterPosition], context, holder.seasons, holder.adapterPosition, holder)
    }

    fun updateCompletedSeasons(position: Int, holder: MyViewHolder) {
        holder.seasonsCompleted.text = populateCompletedSeasons(dataSet[position].volumes, holder)
    }

    private fun populateCompletedSeasons(seasonList: MutableList<Season>, holder: MyViewHolder) : String {
        var completedSeasons = 0

        try {
            seasonList.forEach{
                if(it.isCompleted(it.seenEpisodes, it.totalEpisodes)) completedSeasons++
            }
        } catch (e: Exception) {
            Log.e(Constants.getInstance().logger, "La lista delle stagioni è vuota o nulla. ${e.message}")
            return "0/0"
        }

        if(completedSeasons == seasonList.size)
            holder.completedImage.backgroundTintList = ContextCompat.getColorStateList(context, R.color.light_blue)
        else
            holder.completedImage.backgroundTintList = ContextCompat.getColorStateList(context, R.color.grey)

        return "$completedSeasons/${seasonList.size}"
    }

    private fun setRecyclerAdapter(tvShow: TVShow, context: Context, recyclerView: RecyclerView, position: Int, holder: MyViewHolder) {

        val adapter = SeasonRecyclerAdapter(tvShow.volumes, tvShow, this, position, holder)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        //swipeToDelete(cardList, adapter)
    }

    override fun getItemCount() = dataSet.size
}