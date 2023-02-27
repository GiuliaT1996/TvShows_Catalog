package com.angiuprojects.gamecatalog.adapters

import android.content.Context
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.entities.MainItem
import com.angiuprojects.gamecatalog.entities.implementation.Manga
import com.angiuprojects.gamecatalog.entities.implementation.Season
import com.angiuprojects.gamecatalog.enums.MangaStatusEnum
import com.angiuprojects.gamecatalog.enums.ShowTypeEnum
import com.angiuprojects.gamecatalog.utilities.Constants
import com.angiuprojects.gamecatalog.utilities.ReadWriteJson
import com.angiuprojects.gamecatalog.utilities.Utils

class MainItemRecyclerAdapter(private val dataSet : MutableList<out MainItem>,
                              private val context: Context,
                              private val showTypeEnum: ShowTypeEnum
)
    : FatherRecyclerAdapter<MainItem>(dataSet, context)  {

    override fun onBindViewHolder(holder: MainItemViewHolder, position: Int) {
        holder.name.text = dataSet[holder.adapterPosition].name
        holder.expand.setOnClickListener{ Utils.getInstance().onClickExpandCollapse(holder.seasons, holder.expand)}
        holder.seasonsCompleted.text = populateCompletedSeasons(dataSet[holder.adapterPosition].seasons, holder)

        holder.seasons.visibility = View.GONE

        if(isStatusEnabled()) setMangaStatus(holder, (dataSet[holder.adapterPosition] as Manga).status)

        setRecyclerAdapter(holder.seasons, holder)

        holder.layout.setOnLongClickListener {
            onLongClickOpenEditPopUp(position, dataSet[position], dataSet[position].name, holder, isStatusEnabled(),
                showTypeEnum, MainItem::name.getter, MainItem::name.setter)
            true
        }
    }

    private fun setMangaStatus(holder: MainItemViewHolder, status: MangaStatusEnum) {
        when(status) {
            MangaStatusEnum.COMPLETO -> holder.status.backgroundTintList = ContextCompat.getColorStateList(context, R.color.green)
            MangaStatusEnum.INTERROTTO -> holder.status.backgroundTintList = ContextCompat.getColorStateList(context, R.color.red)
            MangaStatusEnum.IN_CORSO -> holder.status.backgroundTintList = ContextCompat.getColorStateList(context, R.color.grey)
        }
        holder.status.visibility = View.VISIBLE
    }

    fun updateCompletedSeasons(holder: MainItemViewHolder, seasonList: MutableList<Season>) {
        holder.seasonsCompleted.text = populateCompletedSeasons(seasonList, holder)
    }

    private fun populateCompletedSeasons(seasonList: MutableList<Season>, holder: MainItemViewHolder)
    : String {
        var completedSeasons = 0

        try {
            seasonList.forEach{
                if(it.isCompleted(it)) completedSeasons++
            }
        } catch (e: Exception) {
            Log.e(Constants.logger, "La lista delle stagioni è vuota o nulla. ${e.message}")
            return "0/0"
        }

        if(completedSeasons == seasonList.size)
            holder.completedImage.backgroundTintList = ContextCompat.getColorStateList(context, R.color.light_blue)
        else
            holder.completedImage.backgroundTintList = ContextCompat.getColorStateList(context, R.color.grey)

        dataSet[holder.adapterPosition].seasons = seasonList

        return "$completedSeasons/${seasonList.size}"
    }

    fun deleteLastSeason(position: Int) {
        dataSet[position].seasons.removeAt(dataSet[position].seasons.size - 1)
        notifyItemChanged(position)
        //TODO: trovare il modo di espanderlo dopo notifyItemChanged
        //Utils.getInstance().onClickExpandCollapse(holder.seasons, holder.expand)
        ReadWriteJson.getInstance().write(context, false)
    }

    private fun setRecyclerAdapter(recyclerView: RecyclerView, holder: MainItemViewHolder) {
        val adapter = SeasonRecyclerAdapter(dataSet[holder.adapterPosition],
            this, holder.adapterPosition, holder, context, showTypeEnum)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    private fun isStatusEnabled() : Boolean {
        return ShowTypeEnum.MANGA == showTypeEnum
    }
}