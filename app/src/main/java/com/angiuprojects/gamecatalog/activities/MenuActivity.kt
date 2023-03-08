package com.angiuprojects.gamecatalog.activities

import android.content.Context
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.enums.ShowTypeEnum
import com.angiuprojects.gamecatalog.utilities.ReadWriteJson
import com.angiuprojects.gamecatalog.utilities.Utils

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setOnClickMethods()

        val context: Context = this
        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                ReadWriteJson.getInstance().write(context, false)
                finish()
            }
        })
    }

    private fun setOnClickMethods() {
        val tvShowsButton : ImageButton = findViewById(R.id.tv_show)
        tvShowsButton.setOnClickListener{ Utils.getInstance().onClickChangeActivity(TVShowsActivity::class.java, this,
            false, ShowTypeEnum.TV_SHOW.type)}

        val animeButton : ImageButton = findViewById(R.id.anime)
        animeButton.setOnClickListener{ Utils.getInstance().onClickChangeActivity(AnimeActivity::class.java, this,
            false, ShowTypeEnum.ANIME.type)}

        val mangaButton : ImageButton = findViewById(R.id.manga)
        mangaButton.setOnClickListener{ Utils.getInstance().onClickChangeActivity(MangaActivity::class.java, this,
            false, ShowTypeEnum.MANGA.type)}
    }

    override fun onStop() {
        ReadWriteJson.getInstance().write(this, false)
        super.onStop()
    }
}