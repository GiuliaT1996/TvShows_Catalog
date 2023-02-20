package com.angiuprojects.gamecatalog.utilities

import com.angiuprojects.gamecatalog.adapters.SeasonRecyclerAdapter
import com.angiuprojects.gamecatalog.entities.implementation.Season

class SeasonAdapterUtils {

    companion object {

        private lateinit var singleton: SeasonAdapterUtils

        fun initializeUtilsSingleton(): SeasonAdapterUtils {
            singleton = SeasonAdapterUtils()
            return singleton
        }

        fun getInstance(): SeasonAdapterUtils {
            return singleton
        }
    }

    fun setProgressBar(holder: SeasonRecyclerAdapter.SeasonViewHolder, season: Season) {
        holder.episodesCompleted.text = buildString {
            append(season.seenEpisodes)
            append("/")
            append(season.totalEpisodes)
        }

        holder.progressBar.progress = (season.seenEpisodes * 100) / season.totalEpisodes
    }
}