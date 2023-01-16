package com.angiuprojects.gamecatalog.adapters

import android.content.Context
import com.angiuprojects.gamecatalog.entities.implementation.Anime
import com.angiuprojects.gamecatalog.entities.implementation.Season

class AnimeSeasonRecyclerAdapter(private val dataSet : MutableList<Season>,
                                 private val parent: Anime,
    // private val parentRecyclerAdapter: TVShowRecyclerAdapter,
                                 private val position: Int,
    //private val parentViewHolder: TVShowRecyclerAdapter.MyViewHolder,
                                 private val context: Context
)
    : SeasonRecyclerAdapter(dataSet, position, context) {
    override fun onBindViewHolder(holder: SeasonViewHolder, position: Int) {
        //TODO
    }
}