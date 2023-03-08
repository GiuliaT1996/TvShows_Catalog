package com.angiuprojects.gamecatalog.activities

import android.os.Bundle
import android.widget.ImageButton
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.entities.User
import com.angiuprojects.gamecatalog.entities.implementation.TVShow
import com.angiuprojects.gamecatalog.enums.ShowTypeEnum
import com.angiuprojects.gamecatalog.utilities.Constants

class TVShowsActivity : ShowsOpenActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tvshows)

        Constants.user?.tvShowList = setShowsRecyclerView(User::tvShowList, TVShow::name, ShowTypeEnum.TV_SHOW)
        onClickStartAddActivity(ShowTypeEnum.TV_SHOW)

        findViewById<ImageButton>(R.id.filter_button).setOnClickListener {
            setAlphabetRecyclerAdapter(Constants.user!!.tvShowList, this, this, ShowTypeEnum.TV_SHOW)
        }
    }
}