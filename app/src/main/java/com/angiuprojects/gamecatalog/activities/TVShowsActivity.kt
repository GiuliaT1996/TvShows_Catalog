package com.angiuprojects.gamecatalog.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.adapters.TVShowRecyclerAdapter
import com.angiuprojects.gamecatalog.entities.implementation.Season
import com.angiuprojects.gamecatalog.entities.implementation.TVShow
import com.angiuprojects.gamecatalog.utilities.Constants

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

        val season1 = Season(1, 2, 8)
        val tvShow = TVShow("Wednesday", mutableListOf(season1))

        Constants.getInstance().getInstanceTvShows()?.add(tvShow)
    }
}