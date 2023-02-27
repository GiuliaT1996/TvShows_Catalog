package com.angiuprojects.gamecatalog.adapters

import android.content.Context
import android.util.Log
import android.view.View
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
                            val context: Context)
    : FatherRecyclerAdapter<Anime>(dataSet, context) {

    //TODO RIMUOVI STAGIONE

    override fun onBindViewHolder(
        holder: MainItemViewHolder,
        position: Int
    ) {
        val anime = dataSet[holder.adapterPosition]

        holder.name.text = anime.name
        holder.expand.setOnClickListener{ Utils.getInstance().onClickExpandCollapse(holder.seasons, holder.expand)}

        updateCompletedSeasons(holder, anime)

        holder.seasons.visibility = View.GONE

        if(anime.hasSaga && anime.sagas.isNotEmpty())
            Utils.getInstance().setMainItemRecyclerAdapter(dataSet[holder.adapterPosition].sagas,
                fatherContext, holder.seasons, ShowTypeEnum.ANIME)
        else setRecyclerAdapterNoSagas(holder)

        holder.layout.setOnLongClickListener {
            onLongClickOpenEditPopUp(position, anime, anime.name, holder, false,
                ShowTypeEnum.ANIME, Anime::name.getter, Anime::name.setter)
            true
        }
    }

    private fun setRecyclerAdapterNoSagas(holder: MainItemViewHolder) {
        val adapter = AnimeSeasonRecyclerAdapter(dataSet[holder.adapterPosition],
            this, holder.adapterPosition, holder, fatherContext)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(fatherContext)
        holder.seasons.layoutManager = layoutManager

        holder.seasons.setHasFixedSize(true)
        holder.seasons.adapter = adapter
    }

    override fun updateCompletedSeasons(holder: MainItemViewHolder,
                               item: Anime) {
        if(item.hasSaga)
            holder.seasonsCompleted.text = populateProgress(item.sagas, holder, null,
                Saga::isCompleted, item.hasSaga)
        else
            holder.seasonsCompleted.text = populateProgress(item.sagas[0].seasons, holder, Season::isCompleted,
                null, item.hasSaga)
        dataSet[holder.adapterPosition] = item
    }

    private fun <T> populateProgress(list: MutableList<T>,
                                     holder: MainItemViewHolder,
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
            holder.completedImage.backgroundTintList = ContextCompat.getColorStateList(fatherContext, R.color.light_blue)
        else
            holder.completedImage.backgroundTintList = ContextCompat.getColorStateList(fatherContext, R.color.grey)

        return "$completed/${list.size}"
    }

    fun deleteLastSeason(position: Int, holder: MainItemViewHolder) {
        dataSet[holder.adapterPosition].sagas[0].seasons.removeAt(dataSet[holder.adapterPosition].sagas[0].seasons.size - 1)
        notifyItemChanged(position)
        //TODO: find way to expand after notifyItemChanged
        //Utils.getInstance().onClickExpandCollapse(holder.seasons, holder.expand)
        ReadWriteJson.getInstance().write(fatherContext, false)
    }

}