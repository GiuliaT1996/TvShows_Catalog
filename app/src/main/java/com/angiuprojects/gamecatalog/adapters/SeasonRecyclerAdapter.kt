package com.angiuprojects.gamecatalog.adapters

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.entities.MainItem
import com.angiuprojects.gamecatalog.entities.implementation.Season
import com.angiuprojects.gamecatalog.enums.PopUpActionsEnum
import com.angiuprojects.gamecatalog.enums.ShowTypeEnum
import com.angiuprojects.gamecatalog.utilities.*

open class SeasonRecyclerAdapter(private val parent: MainItem,
                                 private val parentRecyclerAdapter: MainItemRecyclerAdapter,
                                 private val position: Int,
                                 private val parentViewHolder: MainItemRecyclerAdapter.MainItemViewHolder,
                                 private val context: Context,
                                 private val showTypeEnum: ShowTypeEnum
): RecyclerView.Adapter<SeasonRecyclerAdapter.SeasonViewHolder>()  {

    private lateinit var dataSet: MutableList<Season>

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

    private lateinit var dialog: Dialog

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.season_view, parent, false)

        dialog = Dialog(context)

        return SeasonViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeasonViewHolder, position: Int) {
        dataSet = parent.seasons

        val season = parent.seasons[holder.adapterPosition]

        holder.name.text = season.name
        setProgressBar(holder, dataSet[holder.adapterPosition])
        holder.plusButton.setOnClickListener{plus(holder)}
        holder.minusButton.setOnClickListener{minus(holder)}
        holder.layout.setOnLongClickListener {
            onLongClickOpenPopUpToEdit(holder.adapterPosition)
            true
        }
    }

    private fun updateParent(){
        parentRecyclerAdapter.updateCompletedSeasons(parentViewHolder, dataSet)
        ReadWriteJson.getInstance().write(context, false)
    }

    private fun onClickOpenPopUp(popUpText: String, action: PopUpActionsEnum, parentPosition: Int) {

        val inflater = context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popUpView: View = inflater.inflate(R.layout.yes_no_message_popup, null)
        dialog.setContentView(popUpView)

        popUpView.findViewById<TextView>(R.id.text).text = popUpText

        val yesButton = popUpView.findViewById<Button>(R.id.si_button)
        yesButton.setOnClickListener{

            when(action) {
                PopUpActionsEnum.DELETE_SHOW -> parentRecyclerAdapter.deleteItem(parentPosition, parent)
                PopUpActionsEnum.DELETE_SEASON -> parentRecyclerAdapter.deleteLastSeason(parentPosition, parentViewHolder)
            }
            dialog.dismiss()
        }

        val noButton = popUpView.findViewById<Button>(R.id.no_button)
        noButton.setOnClickListener{dialog.dismiss()}

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun minus(holder: SeasonViewHolder) {
        if(dataSet[holder.adapterPosition].seenEpisodes == 0) {
            Log.i(Constants.logger, "Non è più possibile rimuovere episodi visti")
            if(dataSet.size > 1) {
                if(dataSet[holder.adapterPosition].number == dataSet.size) {
                    onClickOpenPopUp("Rimuovere stagione?",
                        PopUpActionsEnum.DELETE_SEASON, position)
                }
            } else {
                onClickOpenPopUp("Rimuovere serie?",
                    PopUpActionsEnum.DELETE_SHOW, position)
            }
            return
        }

        dataSet[holder.adapterPosition].seenEpisodes--
        setProgressBar(holder, dataSet[holder.adapterPosition])

        if(dataSet[holder.adapterPosition].seenEpisodes == dataSet[holder.adapterPosition].totalEpisodes - 1)
            updateParent()

        ReadWriteJson.getInstance().write(context, false)
    }

    private fun plus(holder: SeasonViewHolder) {
        if(dataSet[holder.adapterPosition].seenEpisodes == dataSet[holder.adapterPosition].totalEpisodes) {
            if(holder.adapterPosition == dataSet.size - 1) {
                Utils.getInstance().onClickOpenPopUpAddSeason(
                        dialog,
                        dataSet,
                        parentViewHolder.seasons,
                        holder.itemView.rootView.findViewById(android.R.id.content),
                        showTypeEnum,
                        parent,
                        parentRecyclerAdapter,
                        position,
                        parentViewHolder)
                updateParent()
            }
            return
        }
        dataSet[holder.adapterPosition].seenEpisodes++
        setProgressBar(holder, dataSet[holder.adapterPosition])

        if(dataSet[holder.adapterPosition].seenEpisodes == dataSet[holder.adapterPosition].totalEpisodes)
            updateParent()

        ReadWriteJson.getInstance().write(context, false)
    }

    private fun onClickEditSeason(newName: String, newSeen: Int, newTotal: Int, position: Int) {

        if(newName.isNotEmpty() && newName != dataSet[position].name.trim())  dataSet[position].name = newName
        dataSet[position].seenEpisodes = newSeen
        dataSet[position].totalEpisodes = newTotal

        if(newSeen == newTotal || newSeen == newTotal - 1) {
            updateParent()
        }

        parent.seasons = dataSet

        notifyItemChanged(position)
        dialog.dismiss()

        ReadWriteJson.getInstance().write(context, false)
    }

    private fun onLongClickOpenPopUpToEdit(position: Int) {
        val popUpView = Utils.getInstance().openAddEditSeasonPopUp(dialog, dataSet[position].name)

        if(position != 0 && position >= dataSet.size - 1) adjustBackgroundToEdit(popUpView)

        val nameView = popUpView.findViewById<TextView>(R.id.number_input_text)
        val seenView = popUpView.findViewById<AutoCompleteTextView>(R.id.seen_auto_complete)
        seenView.setText(dataSet[position].seenEpisodes.toString())
        val totalView = popUpView.findViewById<AutoCompleteTextView>(R.id.total_auto_complete)
        totalView.setText(dataSet[position].totalEpisodes.toString())

        val editSeason = popUpView.findViewById<Button>(R.id.add_season)
        editSeason.text = context.getString(R.string.modifica_stagione)
        try {
            editSeason.setOnClickListener {
                onClickEditSeason(
                    nameView.text.toString().trim(), seenView.text.toString().toInt(),
                    totalView.text.toString().toInt(), position
                )
            }
        } catch (e: Exception) {
            Log.e(Constants.logger, "I dati sono errati!")
        }
    }

    private fun adjustBackgroundToEdit(popUpView: View) {
        popUpView.findViewById<ImageView>(R.id.line).visibility = View.VISIBLE
        val removeButton = popUpView.findViewById<Button>(R.id.remove)
        removeButton.visibility = View.VISIBLE
        removeButton.setOnClickListener { onClickOpenPopUp("Rimuovere stagione?",
                                            PopUpActionsEnum.DELETE_SEASON, this.position) }
        popUpView.findViewById<ImageView>(R.id.background).layoutParams.height =
            Utils.getInstance().getPixelsFromDP(context, 490)
    }

    private fun setProgressBar(holder: SeasonViewHolder, season: Season) {
        holder.episodesCompleted.text = buildString {
            append(season.seenEpisodes)
            append("/")
            append(season.totalEpisodes)
        }

        holder.progressBar.progress = (season.seenEpisodes * 100) / season.totalEpisodes
    }

    override fun getItemCount() = parent.seasons.size
}