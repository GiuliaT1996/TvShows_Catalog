package com.angiuprojects.gamecatalog.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.adapters.TVShowRecyclerAdapter
import com.angiuprojects.gamecatalog.entities.implementation.TVShow
import com.angiuprojects.gamecatalog.utilities.Constants
import com.angiuprojects.gamecatalog.utilities.Utils

class TVShowsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tvshows)
        val list = Constants.getInstance().getInstanceTvShows()?.sortedBy { it.name }?.toMutableList()
        list?.let { setRecyclerAdapter(it) }
        findViewById<ImageButton>(R.id.add_button).setOnClickListener{addTVShow()}
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

    private fun addTVShow() {
        Utils.getInstance().onClickChangeActivity(AddActivity::class.java, this, true)
    }
}