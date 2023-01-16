package com.angiuprojects.gamecatalog.activities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.adapters.AddSeasonRecyclerAdapter
import com.angiuprojects.gamecatalog.entities.implementation.Season
import com.angiuprojects.gamecatalog.entities.implementation.TVShow
import com.angiuprojects.gamecatalog.queries.Queries
import com.angiuprojects.gamecatalog.utilities.Constants
import com.angiuprojects.gamecatalog.utilities.Utils
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class AddActivity : AppCompatActivity() {

    private lateinit var seasons : MutableList<Season>
    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        seasons = mutableListOf()
        dialog = Dialog(this)
        findViewById<Button>(R.id.add_button).setOnClickListener{onClickAddTvShow()}
        val recyclerView = findViewById<RecyclerView>(R.id.seasons_list)
        findViewById<ImageButton>(R.id.add_season).setOnClickListener{ Utils.getInstance().onClickOpenPopUpAddSeason(dialog, seasons, recyclerView, findViewById(android.R.id.content), null, null, null, null)}
    }

    private fun onClickAddTvShow() {

        val name = findViewById<AutoCompleteTextView>(R.id.name_auto_complete)

        if(Utils.getInstance().launchSnackBar(name.text.toString().trim() == "", "Inserire il nome!", findViewById(android.R.id.content)))
            return

        if(Utils.getInstance().launchSnackBar(seasons.isEmpty(), "Inserire almeno una stagione!", findViewById(android.R.id.content)))
            return

        Queries.getInstance().addUpdate(Constants.getInstance().tvShowDbReference, TVShow(name.text.toString(), seasons))

        Utils.getInstance().onClickChangeActivity(TVShowsActivity::class.java, this, true)
    }
}