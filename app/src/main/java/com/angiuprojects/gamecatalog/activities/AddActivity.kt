package com.angiuprojects.gamecatalog.activities

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.entities.implementation.Anime
import com.angiuprojects.gamecatalog.entities.implementation.Manga
import com.angiuprojects.gamecatalog.entities.implementation.Season
import com.angiuprojects.gamecatalog.entities.implementation.TVShow
import com.angiuprojects.gamecatalog.enums.MangaStatusEnum
import com.angiuprojects.gamecatalog.enums.ShowTypeEnum
import com.angiuprojects.gamecatalog.utilities.*
import com.google.android.material.textfield.TextInputLayout

class AddActivity : AppCompatActivity() {

    private lateinit var seasons : MutableList<Season>
    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        seasons = mutableListOf()

        var showType = ""
        val extras = intent.extras
        if (extras != null) showType = extras.getString("showType")!!

        findViewById<TextView>(R.id.name).text = showType

        val statusSpinner = findViewById<AutoCompleteTextView>(R.id.status_spinner)
        Utils.getInstance().assignAdapterToSpinner(statusSpinner, this)

        if(showType == ShowTypeEnum.MANGA.type)
            findViewById<TextInputLayout>(R.id.status_dropdown).visibility = View.VISIBLE

        dialog = Dialog(this)
        findViewById<Button>(R.id.add_button).setOnClickListener{onClickAdd(showType, statusSpinner)}
        val recyclerView = findViewById<RecyclerView>(R.id.seasons_list)

        findViewById<ImageButton>(R.id.add_season).setOnClickListener{ Utils.getInstance().onClickOpenPopUpAddSeason(dialog,
            seasons, recyclerView, findViewById(android.R.id.content), ShowTypeEnum.getShowTypeEnum(showType), null,
            null, null, null)}
    }

    private fun onClickAdd(showType: String, statusSpinner: AutoCompleteTextView) {

        val name = findViewById<AutoCompleteTextView>(R.id.name_auto_complete).text.toString().trim()

        if(Utils.getInstance().launchSnackBar(name == "", "Inserire il nome!", findViewById(android.R.id.content)))
            return

        if(Utils.getInstance().launchSnackBar(seasons.isEmpty(), "Inserire almeno una stagione!", findViewById(android.R.id.content)))
            return

        when(showType) {
            ShowTypeEnum.TV_SHOW.type -> Constants.user?.tvShowList?.add(TVShow(name, seasons))
            ShowTypeEnum.ANIME.type -> Constants.user?.animeList?.add(Anime()) //TODO!!
            ShowTypeEnum.MANGA.type -> Constants.user?.mangaList?.add(Manga(name, seasons, MangaStatusEnum.getMangaStatusEnum(statusSpinner.text.toString().trim())))
        }
        ReadWriteJson.getInstance().write(this, false)
        changeActivity(showType)
    }

    private fun <T> selectActivity(activity: Class<T>, showType: String) {
        Utils.getInstance().onClickChangeActivity(activity, this, true, showType)
    }

    private fun changeActivity(showType: String) {
        when(showType) {
            ShowTypeEnum.TV_SHOW.type -> selectActivity(TVShowsActivity::class.java, showType)
            //TODO MANCA ANIME
            ShowTypeEnum.MANGA.type -> selectActivity(MangaActivity::class.java, showType)
        }
    }

    override fun onStop() {
        ReadWriteJson.getInstance().write(this, false)
        super.onStop()
    }
}