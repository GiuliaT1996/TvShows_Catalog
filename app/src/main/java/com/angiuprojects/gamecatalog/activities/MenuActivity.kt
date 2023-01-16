package com.angiuprojects.gamecatalog.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.utilities.Utils

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setOnClickMethods()
    }

    private fun setOnClickMethods() {
        val tvShowsButton : ImageButton = findViewById(R.id.tv_show)
        tvShowsButton.setOnClickListener{ Utils.getInstance().onClickChangeActivity(TVShowsActivity::class.java, this, false)}

        var animeButton : ImageButton = findViewById(R.id.anime)

        var mangaButton : ImageButton = findViewById(R.id.manga)
    }
}