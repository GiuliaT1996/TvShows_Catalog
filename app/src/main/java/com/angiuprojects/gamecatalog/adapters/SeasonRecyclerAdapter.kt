package com.angiuprojects.gamecatalog.adapters

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.entities.implementation.Season

open class SeasonRecyclerAdapter(private val dataSet : MutableList<Season>,
                                 private val position: Int,
                                 private val context: Context
): RecyclerView.Adapter<SeasonRecyclerAdapter.SeasonViewHolder>()  {

    class SeasonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView
        var episodesCompleted: TextView
        var plusButton: ImageButton
        var minusButton: ImageButton
        var progressBar: ProgressBar
        var layout: RelativeLayout

        init {
            name = view.findViewById(R.id.name)
            episodesCompleted = view.findViewById(R.id.episodes_completed)
            plusButton = view.findViewById(R.id.plus_button)
            minusButton = view.findViewById(R.id.minus_button)
            progressBar = view.findViewById(R.id.progress_bar)
            layout = view.findViewById(R.id.season_view)
        }
    }

    lateinit var dialog: Dialog

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.season_view, parent, false)

        dialog = Dialog(context)

        return SeasonViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeasonViewHolder, position: Int) { }


    fun setProgressBar(holder: SeasonViewHolder, season: Season) {
        holder.episodesCompleted.text = buildString {
            append(season.seenEpisodes)
            append("/")
            append(season.totalEpisodes)
        }

        holder.progressBar.progress = (season.seenEpisodes * 100) / season.totalEpisodes
    }

    override fun getItemCount() = dataSet.size
}