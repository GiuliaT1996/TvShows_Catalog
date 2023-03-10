package com.angiuprojects.gamecatalog.activities

import android.os.Bundle
import android.widget.ImageButton
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.entities.User
import com.angiuprojects.gamecatalog.entities.implementation.Anime
import com.angiuprojects.gamecatalog.enums.ShowTypeEnum
import com.angiuprojects.gamecatalog.utilities.Constants

class AnimeActivity : ShowsOpenActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime)

        Constants.user?.animeList = setShowsRecyclerView(User::animeList, Anime::name,
            ShowTypeEnum.ANIME)

        onClickStartAddActivity(ShowTypeEnum.ANIME)

        findViewById<ImageButton>(R.id.filter_button).setOnClickListener {
            val toReset = setAlphabetRecyclerAdapter(Constants.user!!.animeList, this,
                this, ShowTypeEnum.ANIME)
            if(toReset) Constants.user?.animeList = setShowsRecyclerView(User::animeList,
                Anime::name, ShowTypeEnum.ANIME)
        }
    }
}