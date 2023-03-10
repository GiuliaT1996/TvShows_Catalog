package com.angiuprojects.gamecatalog.activities

import android.os.Bundle
import android.widget.ImageButton
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.entities.User
import com.angiuprojects.gamecatalog.entities.implementation.Manga
import com.angiuprojects.gamecatalog.enums.ShowTypeEnum
import com.angiuprojects.gamecatalog.utilities.Constants

class MangaActivity : ShowsOpenActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manga)

        Constants.user?.mangaList = setShowsRecyclerView(User::mangaList, Manga::name,
            ShowTypeEnum.MANGA)
        onClickStartAddActivity(ShowTypeEnum.MANGA)

        findViewById<ImageButton>(R.id.filter_button).setOnClickListener {
            val toReset = setAlphabetRecyclerAdapter(Constants.user!!.mangaList, this,
                this, ShowTypeEnum.MANGA)
            if(toReset) Constants.user?.mangaList = setShowsRecyclerView(User::mangaList,
                Manga::name, ShowTypeEnum.MANGA)
        }
    }


}