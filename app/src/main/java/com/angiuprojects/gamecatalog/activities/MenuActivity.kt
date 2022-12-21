package com.angiuprojects.gamecatalog.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.angiuprojects.gamecatalog.R

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setOnClickMethods()
    }

    private fun setOnClickMethods() {
        val tvShowsButton : ImageButton = findViewById(R.id.tv_show)
        tvShowsButton.setOnClickListener{onClickChangeActivity(TVShowsActivity::class.java)}

        var animeButton : ImageButton = findViewById(R.id.anime)

        var mangaButton : ImageButton = findViewById(R.id.manga)
    }

    private fun <T> onClickChangeActivity(activity: Class<T>) {
        val intent = Intent(this, activity)
        startActivity(intent)
    }
}