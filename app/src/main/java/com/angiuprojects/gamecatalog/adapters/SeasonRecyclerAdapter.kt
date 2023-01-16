package com.angiuprojects.gamecatalog.adapters

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.entities.implementation.Season
import com.angiuprojects.gamecatalog.entities.implementation.TVShow
import com.angiuprojects.gamecatalog.queries.Queries
import com.angiuprojects.gamecatalog.utilities.Constants
import com.angiuprojects.gamecatalog.utilities.PopUpActions
import com.angiuprojects.gamecatalog.utilities.Utils

class SeasonRecyclerAdapter(private val dataSet : MutableList<Season>,
                            private val parent: TVShow,
                            private val parentRecyclerAdapter: TVShowRecyclerAdapter,
                            private val position: Int,
                            private val parentViewHolder: TVShowRecyclerAdapter.MyViewHolder,
                            private val context: Context)
    : RecyclerView.Adapter<SeasonRecyclerAdapter.MyViewHolder>() {

    private lateinit var dialog: Dialog

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.season_view, parent, false)

        dialog = Dialog(context)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val season = dataSet[holder.adapterPosition]

        holder.name.text = season.name
        setProgressBar(holder, dataSet[holder.adapterPosition])
        holder.plusButton.setOnClickListener{plus(holder)}
        holder.minusButton.setOnClickListener{minus(holder)}
        holder.layout.setOnLongClickListener {
            onLongClickOpenPopUpToEdit(holder.adapterPosition)
            true
        }
    }

    private fun onLongClickOpenPopUpToEdit(position: Int) {
        val inflater = context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popUpView: View = inflater.inflate(R.layout.add_season_popup, null)
        dialog.setContentView(popUpView)

        popUpView.findViewById<ImageButton>(R.id.close_button).setOnClickListener { dialog.dismiss() }
        val nameView = popUpView.findViewById<TextView>(R.id.number_input_text)
            nameView.text = dataSet[position].name
        val seenView = popUpView.findViewById<AutoCompleteTextView>(R.id.seen_auto_complete)
            seenView.setText(dataSet[position].seenEpisodes.toString())
        val totalView = popUpView.findViewById<AutoCompleteTextView>(R.id.total_auto_complete)
            totalView.setText(dataSet[position].totalEpisodes.toString())
        val editSeason = popUpView.findViewById<Button>(R.id.add_season)
        editSeason.text = context.getString(R.string.modifica_stagione)
        try {
            editSeason.setOnClickListener {
                onClickEditSeason(
                    nameView.text.toString().trim(),
                    seenView.text.toString().toInt(),
                    totalView.text.toString().toInt(),
                    position
                )
            }
        } catch (e: Exception) {
            Log.e(Constants.getInstance().logger, "I dati sono errati!")
        }

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun onClickEditSeason(newName: String, newSeen: Int, newTotal: Int, position: Int) {

        if(newName.isNotEmpty() && newName != dataSet[position].name.trim())  dataSet[position].name = newName
        dataSet[position].seenEpisodes = newSeen
        dataSet[position].totalEpisodes = newTotal

        if(newSeen == newTotal || newSeen == newTotal - 1) {
            updateParent()
        }

        parent.volumes = dataSet
        Queries.getInstance().addUpdate(Constants.getInstance().tvShowDbReference, parent)

        notifyItemChanged(position)
        dialog.dismiss()
    }

    private fun plus(holder: MyViewHolder) {
        if(dataSet[holder.adapterPosition].seenEpisodes == dataSet[holder.adapterPosition].totalEpisodes) {
            if(holder.adapterPosition == dataSet.size - 1) {
                if(Utils.getInstance().onClickOpenPopUpAddSeason(
                    dialog,
                    dataSet,
                    parentViewHolder.seasons,
                    holder.itemView.rootView.findViewById(android.R.id.content),
                    parent,
                    parentRecyclerAdapter,
                    position,
                    parentViewHolder)) updateParent()
            }
            return
        }
        dataSet[holder.adapterPosition].seenEpisodes++
        setProgressBar(holder, dataSet[holder.adapterPosition])
        Queries.getInstance().addUpdate(Constants.getInstance().tvShowDbReference, parent)

        if(dataSet[holder.adapterPosition].seenEpisodes == dataSet[holder.adapterPosition].totalEpisodes)
            updateParent()
    }

    private fun minus(holder: MyViewHolder) {
        if(dataSet[holder.adapterPosition].seenEpisodes == 0) {
            Log.i(Constants.getInstance().logger, "Non è più possibile rimuovere episodi visti")
            if(dataSet.size > 1) {
                if(dataSet[holder.adapterPosition].number == dataSet.size) {
                    onClickOpenPopUp("Rimuovere stagione?",
                        PopUpActions.DELETE_SEASON, position)
                }
            } else {
                onClickOpenPopUp("Rimuovere serie?",
                    PopUpActions.DELETE_SHOW, position)
            }
            return
        }

        dataSet[holder.adapterPosition].seenEpisodes--
        setProgressBar(holder, dataSet[holder.adapterPosition])
        Queries.getInstance().addUpdate(Constants.getInstance().tvShowDbReference, parent)

        if(dataSet[holder.adapterPosition].seenEpisodes == dataSet[holder.adapterPosition].totalEpisodes - 1)
            updateParent()
    }

    private fun onClickOpenPopUp(popUpText: String, action: PopUpActions, position: Int) {

        val inflater = context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popUpView: View = inflater.inflate(R.layout.yes_no_message_popup, null)
        dialog.setContentView(popUpView)

        popUpView.findViewById<TextView>(R.id.text).text = popUpText

        val yesButton = popUpView.findViewById<Button>(R.id.si_button)
        yesButton.setOnClickListener{

            when(action) {
                PopUpActions.DELETE_SHOW -> parentRecyclerAdapter.deleteTvShow(position)
                PopUpActions.DELETE_SEASON -> parentRecyclerAdapter.deleteLastSeason(position)
            }
            dialog.dismiss()
        }

        val noButton = popUpView.findViewById<Button>(R.id.no_button)
        noButton.setOnClickListener{dialog.dismiss()}

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun updateParent(){
        parentRecyclerAdapter.updateCompletedSeasons(parentViewHolder, dataSet)
    }

    private fun setProgressBar(holder: MyViewHolder, season: Season) {
        holder.episodesCompleted.text = buildString {
            append(season.seenEpisodes)
            append("/")
            append(season.totalEpisodes)
        }

        holder.progressBar.progress = (season.seenEpisodes * 100) / season.totalEpisodes
    }

    override fun getItemCount() = dataSet.size
}