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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.entities.implementation.Season
import com.angiuprojects.gamecatalog.entities.implementation.TVShow
import com.angiuprojects.gamecatalog.queries.Queries
import com.angiuprojects.gamecatalog.utilities.Constants
import com.angiuprojects.gamecatalog.utilities.Utils

class TVShowRecyclerAdapter(private val dataSet : MutableList<TVShow>, private val context: Context) : RecyclerView.Adapter<TVShowRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView
        var expand: ImageButton
        var seasonsCompleted: TextView
        var seasons: RecyclerView
        var completedImage: ImageView
        var layout: RelativeLayout

        init {
            name = view.findViewById(R.id.name)
            expand = view.findViewById(R.id.arrow)
            seasonsCompleted = view.findViewById(R.id.seasons_completed)
            seasons = view.findViewById(R.id.seasons_recycler_view)
            completedImage = view.findViewById(R.id.completed)
            layout = view.findViewById(R.id.tv_show_view)
        }
    }

    private lateinit var dialog : Dialog

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tv_show_view, parent, false)

        dialog = Dialog(context)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = dataSet[holder.adapterPosition].name
        holder.expand.setOnClickListener{Utils.getInstance().onClickExpandCollapse(holder.seasons, holder.expand)}
        holder.seasonsCompleted.text = populateCompletedSeasons(dataSet[holder.adapterPosition].seasons, holder)

        holder.seasons.visibility = View.GONE
        setRecyclerAdapter(dataSet[holder.adapterPosition], context, holder.seasons, holder.adapterPosition, holder)

        holder.layout.setOnLongClickListener {
            onClickOpenPopUp(position)
            true
        }
    }

    fun updateCompletedSeasons(holder: MyViewHolder, seasonList: MutableList<Season>) {
        holder.seasonsCompleted.text = populateCompletedSeasons(seasonList, holder)
    }

    fun deleteLastSeason(position: Int) {
        dataSet[position].seasons.removeAt(dataSet[position].seasons.size - 1)
        Queries.getInstance().addUpdate(Constants.getInstance().tvShowDbReference, dataSet[position])
        this.notifyItemChanged(position)
    }

    fun deleteTvShow(position: Int) {
        Queries.getInstance().delete(Constants.getInstance().tvShowDbReference, dataSet[position])
        dataSet.removeAt(position)
        this.notifyItemRemoved(position)
        dialog.dismiss()
    }

    private fun updateShowsName(position: Int, newName: String) {
        if(newName.isNotEmpty() && newName != dataSet[position].name.trim()) {
            Queries.getInstance().delete(Constants.getInstance().tvShowDbReference, dataSet[position])
            dataSet[position].name = newName
            Queries.getInstance().addUpdate(Constants.getInstance().tvShowDbReference, dataSet[position])
            this.notifyItemChanged(position)
            dialog.dismiss()
        }
    }

    private fun onClickOpenPopUp(position: Int) {

        val inflater = context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popUpView: View = inflater.inflate(R.layout.update_delete_show_popup, null)
        dialog.setContentView(popUpView)

        val newName = popUpView.findViewById<AutoCompleteTextView>(R.id.name_auto_complete)
        newName.setText(dataSet[position].name)

        val updateButton = popUpView.findViewById<Button>(R.id.update)
        updateButton.setOnClickListener{updateShowsName(position, newName.text.toString().trim())}

        val removeButton = popUpView.findViewById<Button>(R.id.remove)
        removeButton.setOnClickListener{deleteTvShow(position)}

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun populateCompletedSeasons(seasonList: MutableList<Season>, holder: MyViewHolder) : String {
        var completedSeasons = 0

        try {
            seasonList.forEach{
                if(it.isCompleted(it.seenEpisodes, it.totalEpisodes)) completedSeasons++
            }
        } catch (e: Exception) {
            Log.e(Constants.getInstance().logger, "La lista delle stagioni è vuota o nulla. ${e.message}")
            return "0/0"
        }

        if(completedSeasons == seasonList.size)
            holder.completedImage.backgroundTintList = ContextCompat.getColorStateList(context, R.color.light_blue)
        else
            holder.completedImage.backgroundTintList = ContextCompat.getColorStateList(context, R.color.grey)

        return "$completedSeasons/${seasonList.size}"
    }

    private fun setRecyclerAdapter(tvShow: TVShow, context: Context, recyclerView: RecyclerView, position: Int, holder: MyViewHolder) {

        val adapter = TvShowSeasonRecyclerAdapter(tvShow.seasons, tvShow, this, position, holder, context)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun getItemCount() = dataSet.size
}