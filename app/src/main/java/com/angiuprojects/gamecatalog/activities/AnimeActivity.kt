package com.angiuprojects.gamecatalog.activities

import android.content.Context
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.adapters.AnimeRecyclerAdapter
import com.angiuprojects.gamecatalog.entities.implementation.Anime
import com.angiuprojects.gamecatalog.enums.ShowTypeEnum
import com.angiuprojects.gamecatalog.utilities.Constants
import com.angiuprojects.gamecatalog.utilities.Utils

class AnimeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime)

        val recyclerView = findViewById<RecyclerView>(R.id.anime_recycler_view)

        if(Constants.user != null && Constants.user!!.animeList.isNotEmpty()) {
            Constants.user!!.animeList = Constants.user!!.animeList.sortedBy { it.name }.toMutableList()
            setRecyclerAdapter(Constants.user!!.animeList, this, recyclerView)
        }

        findViewById<ImageButton>(R.id.add_button).setOnClickListener{
            Utils.getInstance().onClickChangeActivity(
                AddActivity::class.java, this, true, ShowTypeEnum.ANIME.type)}
    }

    private fun setRecyclerAdapter(itemList: MutableList<Anime>,
                                   context: Context,
                                   recyclerView: RecyclerView
    ) {

        val adapter = AnimeRecyclerAdapter(itemList, context)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }
}