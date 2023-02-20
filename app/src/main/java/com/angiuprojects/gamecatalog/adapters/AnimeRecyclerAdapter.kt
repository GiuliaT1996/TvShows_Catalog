package com.angiuprojects.gamecatalog.adapters

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.entities.implementation.Anime
import com.angiuprojects.gamecatalog.entities.implementation.Saga
import com.angiuprojects.gamecatalog.entities.implementation.Season
import com.angiuprojects.gamecatalog.enums.ShowTypeEnum
import com.angiuprojects.gamecatalog.utilities.Constants
import com.angiuprojects.gamecatalog.utilities.ReadWriteJson
import com.angiuprojects.gamecatalog.utilities.Utils
import java.util.function.BiFunction

class AnimeRecyclerAdapter (private val dataSet : MutableList<Anime>,
                                 private val context: Context)
    : RecyclerView.Adapter<MainItemRecyclerAdapter.MainItemViewHolder>() {

    private lateinit var dialog : Dialog

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainItemRecyclerAdapter.MainItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_item_view, parent, false)

        dialog = Dialog(context)

        return MainItemRecyclerAdapter.MainItemViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MainItemRecyclerAdapter.MainItemViewHolder,
        position: Int
    ) {
        val anime = dataSet[holder.adapterPosition]

        holder.name.text = anime.name
        holder.expand.setOnClickListener{ Utils.getInstance().onClickExpandCollapse(holder.seasons, holder.expand)}

        updateCompletedSeasons(holder, anime)

        holder.seasons.visibility = View.GONE

        if(anime.hasSaga && anime.sagas.isNotEmpty())
            Utils.getInstance().setMainItemRecyclerAdapter(dataSet[holder.adapterPosition].sagas,
                context, holder.seasons, ShowTypeEnum.ANIME)
        else setRecyclerAdapterNoSagas(holder)

        //TODO onLongClickEdit
    }

    private fun setRecyclerAdapterNoSagas(holder: MainItemRecyclerAdapter.MainItemViewHolder) {
        val adapter = AnimeSeasonRecyclerAdapter(dataSet[holder.adapterPosition],
            this, holder.adapterPosition, holder, context)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        holder.seasons.layoutManager = layoutManager

        holder.seasons.setHasFixedSize(true)
        holder.seasons.adapter = adapter
    }

    fun updateCompletedSeasons(holder: MainItemRecyclerAdapter.MainItemViewHolder,
                               anime: Anime) {
        if(anime.hasSaga)
            holder.seasonsCompleted.text = populateProgress(anime.sagas, holder, null,
                Saga::isCompleted, anime.hasSaga)
        else
            holder.seasonsCompleted.text = populateProgress(anime.sagas[0].seasons, holder, Season::isCompleted,
                null, anime.hasSaga)
        dataSet[holder.adapterPosition] = anime
    }

    private fun <T> populateProgress(list: MutableList<T>,
                                     holder: MainItemRecyclerAdapter.MainItemViewHolder,
                                     seasonFunction: BiFunction<Season, Season ,Boolean>?,
                                     sagaFunction: BiFunction<Saga, MutableList<Season>, Boolean>?,
                                     hasSaga: Boolean ) :String {
        var completed = 0

        try {
            list.forEach{
                if(hasSaga) run {
                    if (sagaFunction!!.apply(it as Saga, (it as Saga).seasons)) completed++
                } else {
                    if(seasonFunction!!.apply(it as Season, it as Season)) completed++
                }
            }
        } catch (e: Exception) {
            Log.e(Constants.logger, "La lista è vuota o nulla. ${e.message}")
            return "0/0"
        }

        if(completed == list.size)
            holder.completedImage.backgroundTintList = ContextCompat.getColorStateList(context, R.color.light_blue)
        else
            holder.completedImage.backgroundTintList = ContextCompat.getColorStateList(context, R.color.grey)

        return "$completed/${list.size}"
    }

    fun deleteLastSeason(position: Int, holder: MainItemRecyclerAdapter.MainItemViewHolder) {
        dataSet[holder.adapterPosition].sagas[0].seasons.removeAt(dataSet[holder.adapterPosition].sagas[0].seasons.size - 1)
        notifyItemChanged(position)
        //TODO: find way to expand after notifyItemChanged
        //Utils.getInstance().onClickExpandCollapse(holder.seasons, holder.expand)
        ReadWriteJson.getInstance().write(context, false)
    }

    fun deleteItem(position: Int) {
        this.notifyItemRemoved(position)
        dataSet.removeAt(position)
        ReadWriteJson.getInstance().write(context, false)
        dialog.dismiss()
    }

    override fun getItemCount() = dataSet.size

}