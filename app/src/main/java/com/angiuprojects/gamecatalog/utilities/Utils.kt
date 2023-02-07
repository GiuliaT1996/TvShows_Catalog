package com.angiuprojects.gamecatalog.utilities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.adapters.AddSeasonRecyclerAdapter
import com.angiuprojects.gamecatalog.adapters.MainItemRecyclerAdapter
import com.angiuprojects.gamecatalog.adapters.SeasonRecyclerAdapter
import com.angiuprojects.gamecatalog.entities.MainItem
import com.angiuprojects.gamecatalog.entities.implementation.Anime
import com.angiuprojects.gamecatalog.entities.implementation.Manga
import com.angiuprojects.gamecatalog.entities.implementation.Season
import com.angiuprojects.gamecatalog.entities.implementation.TVShow
import com.angiuprojects.gamecatalog.enums.MangaStatusEnum
import com.angiuprojects.gamecatalog.enums.ShowTypeEnum
import com.google.android.material.snackbar.Snackbar


class Utils {

    companion object {

        private lateinit var singleton: Utils

        fun initializeUtilsSingleton(): Utils {
            singleton = Utils()
            return singleton
        }

        fun getInstance(): Utils {
            return singleton
        }
    }

    fun checkIfNameAlreadyExists(newName: String, position: Int?, showTypeEnum: ShowTypeEnum) : Boolean{
        return when(showTypeEnum) {
            ShowTypeEnum.MANGA -> genericCheckName(Constants.user?.mangaList, newName, position)
            ShowTypeEnum.TV_SHOW -> genericCheckName(Constants.user?.tvShowList, newName, position)
            ShowTypeEnum.ANIME -> true //TODO
        }
    }

    private fun genericCheckName(items: MutableList<out MainItem>? , newName: String, position: Int?) : Boolean {
        return items?.filterIndexed{ index, it ->  it.name == newName && (position == null || index != position) }
            .isNullOrEmpty()
    }

    fun getPixelsFromDP(context: Context, dps: Int) : Int {
        val scale: Float = context.resources.displayMetrics.density
        return (dps * scale + 0.5f).toInt()
    }

    fun onClickExpandCollapse(recyclerView: RecyclerView, expandButton: ImageButton) {
        if(recyclerView.visibility == View.GONE) {
            Log.i(Constants.logger, "EXPAND")
            //expand
            recyclerView.visibility = View.VISIBLE
            expandButton.rotation = 180F
        } else {
            Log.i(Constants.logger, "COLLAPSE")
            recyclerView.visibility = View.GONE
            expandButton.rotation = -90F
        }
    }

    fun <T> onClickChangeActivity(activity: Class<T>, context: Context, closePrevious: Boolean, showType: String) {
        if(context is Activity && closePrevious) context.finish()
        val intent = Intent(context, activity)
        intent.putExtra("showType", showType)
        context.startActivity(intent)
    }

    fun onClickOpenPopUpAddSeason(dialog: Dialog,
                                  seasons: MutableList<Season>,
                                  recyclerView: RecyclerView,
                                  snackBarView: View,
                                  showTypeEnum: ShowTypeEnum,
                                  parentMainItem: MainItem?,
                                  parentRecyclerAdapter: MainItemRecyclerAdapter?,
                                  position: Int?,
                                  parentViewHolder: MainItemRecyclerAdapter.MainItemViewHolder?) {

        val popUpView = openAddEditSeasonPopUp(dialog, buildString {
            append("Stagione ")
            append(seasons.size + 1)
        })

        popUpView.findViewById<Button>(R.id.add_season).setOnClickListener{ onClickAddSeason(popUpView, dialog, seasons, recyclerView, snackBarView,
            parentMainItem, parentRecyclerAdapter, position, parentViewHolder, showTypeEnum) }
    }

    fun openAddEditSeasonPopUp(dialog: Dialog, seasonName: String) : View {
        val inflater = dialog.context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popUpView: View = inflater.inflate(R.layout.add_season_popup, null)
        dialog.setContentView(popUpView)

        popUpView.findViewById<TextView>(R.id.number_input_text).text = seasonName

        popUpView.findViewById<ImageButton>(R.id.close_button).setOnClickListener{dialog.dismiss()}

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        return popUpView
    }

    private fun onClickAddSeason(popUpView: View,
                                 dialog: Dialog,
                                 seasons: MutableList<Season>,
                                 recyclerView: RecyclerView,
                                 snackBarView: View,
                                 parentMainItem: MainItem?,
                                 parentRecyclerAdapter: MainItemRecyclerAdapter?,
                                 position: Int?,
                                 parentViewHolder: MainItemRecyclerAdapter.MainItemViewHolder?,
                                 showTypeEnum: ShowTypeEnum
    ) {

        var seenEpisodes = 0
        var totalEpisodes = 0

        try {
            seenEpisodes = Integer.parseInt(popUpView.findViewById<AutoCompleteTextView>(R.id.seen_auto_complete).text.toString())
            totalEpisodes = Integer.parseInt(popUpView.findViewById<AutoCompleteTextView>(R.id.total_auto_complete).text.toString())
        } catch(e: Exception) {
            if(launchSnackBar(popUpView.findViewById<AutoCompleteTextView>(R.id.seen_auto_complete).text.toString() == ""
                        || popUpView.findViewById<AutoCompleteTextView>(R.id.total_auto_complete).text.toString() == "",
                    "Inserire episodi visti e totali!",
                        snackBarView))
                return
        }

        val season = Season(seasons.size + 1, seenEpisodes, totalEpisodes)
        seasons.add(season)

        seasons.sortedBy { it.name }
        parentMainItem?.seasons = seasons.sortedBy { it.name }.toMutableList()

        setSeasonRecyclerAdapter(seasons, dialog.context, recyclerView, parentMainItem,
            parentRecyclerAdapter, position, parentViewHolder, showTypeEnum)
        dialog.dismiss()
    }

    private fun setSeasonRecyclerAdapter(seasons: MutableList<Season>,
                                         context: Context,
                                         recyclerView: RecyclerView,
                                         parentMainItem: MainItem?,
                                         parentRecyclerAdapter: MainItemRecyclerAdapter?,
                                         position: Int?,
                                         parentViewHolder: MainItemRecyclerAdapter.MainItemViewHolder?,
                                         showTypeEnum: ShowTypeEnum
    ) {

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        recyclerView.setHasFixedSize(true)

        if(parentMainItem != null
            && parentRecyclerAdapter != null
            && position != null
            && parentViewHolder != null) {
            recyclerView.adapter = SeasonRecyclerAdapter(parentMainItem, parentRecyclerAdapter, position, parentViewHolder, context, showTypeEnum)
            parentRecyclerAdapter.updateCompletedSeasons(parentViewHolder, parentMainItem.seasons)
        }
        else recyclerView.adapter = AddSeasonRecyclerAdapter(seasons)

    }

    fun launchSnackBar(condition: Boolean, message: String, snackBarView: View) : Boolean {

        if(condition) {
            Snackbar.make(
                snackBarView, message,
                Snackbar.LENGTH_LONG
            ).show()
            return true
        }
        return false
    }

    fun setMainItemRecyclerAdapter(itemList: MutableList<out MainItem>,
    context: Context, recyclerView: RecyclerView, showTypeEnum: ShowTypeEnum
    ) {

        val adapter = MainItemRecyclerAdapter(itemList, context, showTypeEnum)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    fun assignAdapterToSpinner(filterSpinner: AutoCompleteTextView, context: Context) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, MangaStatusEnum.getList())
        filterSpinner.setAdapter(adapter)

        filterSpinner.setDropDownBackgroundDrawable(
            ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.round_background,
                null
            ))
    }
}