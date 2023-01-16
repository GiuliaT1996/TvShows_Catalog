package com.angiuprojects.gamecatalog.utilities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.angiuprojects.gamecatalog.adapters.TvShowSeasonRecyclerAdapter
import com.angiuprojects.gamecatalog.adapters.TVShowRecyclerAdapter
import com.angiuprojects.gamecatalog.entities.implementation.Season
import com.angiuprojects.gamecatalog.entities.implementation.TVShow
import com.angiuprojects.gamecatalog.queries.Queries
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

    fun onClickExpandCollapse(recyclerView: RecyclerView, expandButton: ImageButton) {
        if(recyclerView.visibility == View.GONE) {
            //expand
            recyclerView.visibility = View.VISIBLE
            expandButton.rotation = 180F
        } else {
            recyclerView.visibility = View.GONE
            expandButton.rotation = -90F
        }
    }

    fun <T> onClickChangeActivity(activity: Class<T>, context: Context, closePrevious: Boolean) {
        if(context is Activity && closePrevious) context.finish()
        val intent = Intent(context, activity)
        context.startActivity(intent)
    }

    fun onClickOpenPopUpAddSeason(dialog: Dialog,
                                  seasons: MutableList<Season>,
                                  recyclerView: RecyclerView,
                                  snackBarView: View,
                                  parentTvShow: TVShow?,
                                  parentRecyclerAdapter: TVShowRecyclerAdapter?,
                                  position: Int?,
                                  parentViewHolder: TVShowRecyclerAdapter.MyViewHolder?) : Boolean {

        val inflater = dialog.context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popUpView: View = inflater.inflate(R.layout.add_season_popup, null)
        dialog.setContentView(popUpView)

        val name = popUpView.findViewById<TextView>(R.id.number_input_text)

        name.text = buildString {
            append("Stagione ")
            append(seasons.size + 1)
        }

        val addSeasonButton = popUpView.findViewById<Button>(R.id.add_season)
        addSeasonButton.setOnClickListener{ onClickAddSeason(popUpView, dialog, seasons, recyclerView, snackBarView,
            parentTvShow, parentRecyclerAdapter, position, parentViewHolder) }

        popUpView.findViewById<ImageButton>(R.id.close_button).setOnClickListener{dialog.dismiss()}

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        return true
    }

    private fun onClickAddSeason(popUpView: View,
                                 dialog: Dialog,
                                 seasons: MutableList<Season>,
                                 recyclerView: RecyclerView,
                                 snackBarView: View,
                                 parentTvShow: TVShow?,
                                 parentRecyclerAdapter: TVShowRecyclerAdapter?,
                                 position: Int?,
                                 parentViewHolder: TVShowRecyclerAdapter.MyViewHolder?) {

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
        parentTvShow?.seasons = seasons.sortedBy { it.name }.toMutableList()

        if(parentTvShow != null) Queries.getInstance().addUpdate(Constants.getInstance().tvShowDbReference, parentTvShow)

        setRecyclerAdapter(seasons, dialog.context, recyclerView, parentTvShow, parentRecyclerAdapter, position, parentViewHolder)
        dialog.dismiss()
    }

    private fun setRecyclerAdapter(seasons: MutableList<Season>,
                                   context: Context,
                                   recyclerView: RecyclerView,
                                   parentTvShow: TVShow?,
                                   parentRecyclerAdapter: TVShowRecyclerAdapter?,
                                   position: Int?,
                                   parentViewHolder: TVShowRecyclerAdapter.MyViewHolder?) {

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        recyclerView.setHasFixedSize(true)

        if(parentTvShow != null
            && parentRecyclerAdapter != null
            && position != null
            && parentViewHolder != null)  recyclerView.adapter = TvShowSeasonRecyclerAdapter(seasons, parentTvShow, parentRecyclerAdapter, position, parentViewHolder, context)
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
}