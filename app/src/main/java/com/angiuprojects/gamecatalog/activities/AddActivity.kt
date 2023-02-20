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
import com.angiuprojects.gamecatalog.adapters.AddSagaRecyclerAdapter
import com.angiuprojects.gamecatalog.entities.implementation.*
import com.angiuprojects.gamecatalog.enums.MangaStatusEnum
import com.angiuprojects.gamecatalog.enums.ShowTypeEnum
import com.angiuprojects.gamecatalog.utilities.Constants
import com.angiuprojects.gamecatalog.utilities.ReadWriteJson
import com.angiuprojects.gamecatalog.utilities.Utils
import com.google.android.material.textfield.TextInputLayout

class AddActivity : AppCompatActivity() {

    private lateinit var seasons : MutableList<Season>
    private lateinit var sagas : MutableList<Saga>
    private lateinit var dialog : Dialog
    private var hasSaga = false
    private var showType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        seasons = mutableListOf()
        sagas = mutableListOf()
        dialog = Dialog(this)

        val extras = intent.extras
        if (extras != null) showType = extras.getString("showType")!!

        findViewById<TextView>(R.id.name).text = showType

        val statusSpinner = findViewById<AutoCompleteTextView>(R.id.status_spinner)
        Utils.getInstance().assignAdapterToSpinner(statusSpinner, this)


        if(showType == ShowTypeEnum.ANIME.type)
            hasSagaPopUp(statusSpinner)
        else {
            if(showType == ShowTypeEnum.MANGA.type)
                findViewById<TextInputLayout>(R.id.status_dropdown).visibility = View.VISIBLE
            handleMainItem(statusSpinner)
        }

    }

    private fun handleAnimeWithSaga() {
        findViewById<TextView>(R.id.season_name).text = getString(R.string.sagas)

        findViewById<Button>(R.id.add_button).setOnClickListener{onClickAddAnimeWithSagas()}

        findViewById<ImageButton>(R.id.add_season).setOnClickListener { openAddSagaPopUp() }
    }

    private fun openAddSagaPopUp() {
        val sagaDialog = Dialog(this)
        val inflater = sagaDialog.context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popUpView: View = inflater.inflate(R.layout.add_saga_pop_up, null)
        sagaDialog.setContentView(popUpView)

        popUpView.findViewById<ImageButton>(R.id.close_button).setOnClickListener{sagaDialog.dismiss()}
        popUpView.findViewById<Button>(R.id.add_button).setOnClickListener{addSaga(popUpView, sagaDialog)}

        //Seasons recycler view
        val recyclerView = popUpView.findViewById<RecyclerView>(R.id.seasons_list)

        popUpView.findViewById<ImageButton>(R.id.add_season).setOnClickListener {
            Utils.getInstance().onClickOpenPopUpAddSeason(
                dialog,
                seasons,
                recyclerView,
                findViewById(android.R.id.content),
                ShowTypeEnum.getShowTypeEnum(showType),
                null,
                null,
                null,
                null,
                null
            )
        }

        sagaDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        sagaDialog.show()
    }

    private fun addSaga(popUpView: View, dialog: Dialog) {
        val name = popUpView.findViewById<AutoCompleteTextView>(R.id.name_auto_complete).text.toString().trim()

        if(Utils.getInstance().launchSnackBar(name == "", "Inserire il nome!",
                popUpView))
            return

        if(Utils.getInstance().launchSnackBar(seasons.isEmpty(), "Inserire almeno una stagione!",
                popUpView))
            return

        if(Utils.getInstance().launchSnackBar(!Utils.getInstance().genericCheckName(sagas, name, null),
                "Nome già esistente!", popUpView))
            return

        sagas.add(Saga(name, seasons))
        seasons = mutableListOf()
        dialog.dismiss()

        //Sagas recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.seasons_list)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = AddSagaRecyclerAdapter(sagas)
    }

    private fun handleMainItem(statusSpinner: AutoCompleteTextView) {
        findViewById<Button>(R.id.add_button).setOnClickListener{onClickAdd(statusSpinner)}
        val recyclerView = findViewById<RecyclerView>(R.id.seasons_list)

        findViewById<ImageButton>(R.id.add_season).setOnClickListener {
            Utils.getInstance().onClickOpenPopUpAddSeason(
                dialog,
                seasons,
                recyclerView,
                findViewById(android.R.id.content),
                ShowTypeEnum.getShowTypeEnum(showType),
                null,
                null,
                null,
                null,
                null
            )
        }
    }

    private fun hasSagaPopUp(statusSpinner: AutoCompleteTextView) {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popUpView: View = inflater.inflate(R.layout.yes_no_message_popup, null)
        dialog.setContentView(popUpView)

        popUpView.findViewById<TextView>(R.id.text).text = getString(R.string.has_saga)

        popUpView.findViewById<Button>(R.id.si_button).setOnClickListener{
            hasSaga = true
            dialog.dismiss()
            handleAnimeWithSaga()
        }

        popUpView.findViewById<Button>(R.id.no_button).setOnClickListener{
            dialog.dismiss()
            handleMainItem(statusSpinner)
        }

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun checkInfo(name: String) : Boolean {
        if(Utils.getInstance().launchSnackBar(name == "", "Inserire il nome!",
                findViewById(android.R.id.content)))
            return false

        if(hasSaga) {
            if(Utils.getInstance().launchSnackBar(sagas.isEmpty(), "Inserire almeno una saga!",
                    findViewById(android.R.id.content)))
                return false
        } else {
            if(Utils.getInstance().launchSnackBar(seasons.isEmpty(), "Inserire almeno una stagione!",
                    findViewById(android.R.id.content)))
                return false
        }

        if(Utils.getInstance().launchSnackBar(!Utils.getInstance()
                .checkIfNameAlreadyExists(name, null, ShowTypeEnum.getShowTypeEnum(showType)),
                "Nome già esistente!", findViewById(android.R.id.content)))
            return false

        return true
    }

    private fun onClickAddAnimeWithSagas() {
        val name = findViewById<AutoCompleteTextView>(R.id.name_auto_complete).text.toString().trim()

        if(!checkInfo(name))
            return

        Constants.user?.animeList?.add(Anime(name, sagas, true))

        ReadWriteJson.getInstance().write(this, false)
        changeActivity()
    }

    private fun onClickAdd(statusSpinner: AutoCompleteTextView) {
        val name = findViewById<AutoCompleteTextView>(R.id.name_auto_complete).text.toString().trim()

        if(!checkInfo(name))
            return

        when(showType) {
            ShowTypeEnum.TV_SHOW.type -> Constants.user?.tvShowList?.add(TVShow(name, seasons))
            ShowTypeEnum.ANIME.type -> Constants.user?.animeList?.add(createSimpleAnime(name, seasons))
            ShowTypeEnum.MANGA.type -> Constants.user?.mangaList?.add(Manga(name, seasons,
                MangaStatusEnum.getMangaStatusEnum(statusSpinner.text.toString().trim())))
        }
        ReadWriteJson.getInstance().write(this, false)
        changeActivity()
    }

    private fun createSimpleAnime(name: String, seasons: MutableList<Season>) : Anime {
        val sagas: MutableList<Saga> = mutableListOf(Saga(name, seasons))
        return Anime(name, sagas, false)
    }

    private fun <T> selectActivity(activity: Class<T>) {
        Utils.getInstance().onClickChangeActivity(activity, this, true, showType)
    }

    private fun changeActivity() {
        when(showType) {
            ShowTypeEnum.TV_SHOW.type -> selectActivity(TVShowsActivity::class.java)
            ShowTypeEnum.ANIME.type -> selectActivity(AnimeActivity::class.java)
            ShowTypeEnum.MANGA.type -> selectActivity(MangaActivity::class.java)
        }
    }

    override fun onStop() {
        ReadWriteJson.getInstance().write(this, false)
        super.onStop()
    }
}