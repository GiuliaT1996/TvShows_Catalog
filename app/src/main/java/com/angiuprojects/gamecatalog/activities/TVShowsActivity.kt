package com.angiuprojects.gamecatalog.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.adapters.TVShowRecyclerAdapter
import com.angiuprojects.gamecatalog.entities.implementation.Episode
import com.angiuprojects.gamecatalog.entities.implementation.Season
import com.angiuprojects.gamecatalog.entities.implementation.TVShow
import com.angiuprojects.gamecatalog.utilities.Constants
import java.util.*

class TVShowsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tvshows)

        addTvShow()
        Constants.getInstance().getInstanceTvShows()?.let { setRecyclerAdapter(it) }
    }

    private fun setRecyclerAdapter(tvShowList: MutableList<TVShow>) {

        val adapter = TVShowRecyclerAdapter(tvShowList, this)
        val recyclerView = findViewById<RecyclerView>(R.id.tv_show_recycler_view)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        //swipeToDelete(cardList, adapter)
    }

    private fun addTvShow() {

        val episode1 = Episode(1,"Wednesday's Child Is Full of Woe", seen = true)
        val episode2 = Episode(2,"Woe Is the Loneliest Number", seen = true)
        val episode3 = Episode(3,"Friend or Woe", seen = false)
        val episode4 = Episode(4,"Woe What a Night", seen = false)
        val episode5 = Episode(5,"You Reap What You Woe", seen = false)
        val episode6 = Episode(6,"Quid Pro Woe", seen = false)
        val episode7 = Episode(7,"If You Don't Woe Me By Now", seen = false)
        val episode8 = Episode(8,"A Murder of Woes", seen = false)
        val season1 = Season(1, "Prima Stagione", mutableListOf(episode1, episode2, episode3, episode4, episode5, episode6, episode7, episode8))
        val tvShow = TVShow("Wednesday", mutableListOf(season1))

        Constants.getInstance().getInstanceTvShows()?.add(tvShow)
    }
}