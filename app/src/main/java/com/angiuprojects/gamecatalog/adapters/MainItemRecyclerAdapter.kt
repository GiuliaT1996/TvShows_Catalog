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
import com.angiuprojects.gamecatalog.entities.MainItem
import com.angiuprojects.gamecatalog.entities.implementation.Manga
import com.angiuprojects.gamecatalog.entities.implementation.Season
import com.angiuprojects.gamecatalog.utilities.*

open class MainItemRecyclerAdapter(private val dataSet : MutableList<out MainItem>,
                                   private val context: Context,
                                   private val showTypeEnum: ShowTypeEnum)
    : RecyclerView.Adapter<MainItemRecyclerAdapter.MainItemViewHolder>() {

    class MainItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView
        var expand: ImageButton
        var seasonsCompleted: TextView
        var seasons: RecyclerView
        var completedImage: ImageView
        var status: ImageView
        var layout: RelativeLayout

        init {
            name = view.findViewById(R.id.name)
            expand = view.findViewById(R.id.arrow)
            seasonsCompleted = view.findViewById(R.id.seasons_completed)
            seasons = view.findViewById(R.id.seasons_recycler_view)
            completedImage = view.findViewById(R.id.completed)
            status = view.findViewById(R.id.status)
            layout = view.findViewById(R.id.tv_show_view)
        }
    }

    private lateinit var dialog : Dialog

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_item_view, parent, false)

        dialog = Dialog(context)

        return MainItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainItemViewHolder, position: Int) {
        holder.name.text = dataSet[holder.adapterPosition].name
        holder.expand.setOnClickListener{ Utils.getInstance().onClickExpandCollapse(holder.seasons, holder.expand)}
        holder.seasonsCompleted.text = populateCompletedSeasons(dataSet[holder.adapterPosition].seasons, holder)

        holder.seasons.visibility = View.GONE

        if(isStatusEnabled()) setMangaStatus(holder, (dataSet[holder.adapterPosition] as Manga).status)

        setRecyclerAdapter(holder.seasons, holder)

        holder.layout.setOnLongClickListener {
            onClickOpenPopUp(position, dataSet[position], holder)
            true
        }
    }

    private fun setMangaStatus(holder: MainItemViewHolder, status: MangaStatusEnum) {
        when(status) {
            MangaStatusEnum.COMPLETO -> holder.status.setBackgroundColor(Color.GREEN)
            MangaStatusEnum.INTERROTTO -> holder.status.setBackgroundColor(Color.RED)
            MangaStatusEnum.IN_CORSO -> holder.status.setBackgroundColor(Color.GRAY)
        }
        holder.status.visibility = View.VISIBLE
    }

    private fun onClickOpenPopUp(position: Int, item: MainItem, holder: MainItemViewHolder) {

        val inflater = context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var popUpView: View = inflater.inflate(R.layout.update_delete_show_popup, null)

        var status: AutoCompleteTextView? = null

        if(isStatusEnabled() && dataSet[holder.adapterPosition] is Manga) {
            popUpView = inflater.inflate(R.layout.update_delete_manga_popup, null)
            status = handlePopUpStatus(popUpView, dataSet[holder.adapterPosition] as Manga)
        }

        dialog.setContentView(popUpView)

        val newName = popUpView.findViewById<AutoCompleteTextView>(R.id.name_auto_complete)
        newName.setText(item.name)

        val updateButton = popUpView.findViewById<Button>(R.id.update)
        updateButton.setOnClickListener{updateShowsName(position, newName.text.toString().trim(), item, status)}

        val removeButton = popUpView.findViewById<Button>(R.id.remove)
        removeButton.setOnClickListener{deleteItem(position, item)}

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun handlePopUpStatus(popUpView: View, manga: Manga?) : AutoCompleteTextView {
        val filterSpinner = popUpView.findViewById<AutoCompleteTextView>(R.id.status_spinner)
        Utils.getInstance().assignAdapterToSpinner(filterSpinner, context)

        filterSpinner.setText(manga?.status.toString())
        return filterSpinner
    }

    open fun deleteItem(position: Int, item: MainItem) {
        this.notifyItemRemoved(position)
        dataSet.removeAt(position)
        ReadWriteJson.getInstance().write(context, false)
        dialog.dismiss()
    }

    private fun updateShowsName(position: Int, newName: String, item: MainItem, status: AutoCompleteTextView?) {

        if(status != null && item is Manga)
            item.status = MangaStatusEnum.getMangaStatusEnum(status.text.toString().trim())

        if(newName.isNotEmpty() && newName != item.name.trim()) {
            item.name = newName
        }
        this.notifyItemChanged(position)
        ReadWriteJson.getInstance().write(context, false)
        dialog.dismiss()
    }

    fun updateCompletedSeasons(holder: MainItemViewHolder, seasonList: MutableList<Season>) {
        holder.seasonsCompleted.text = populateCompletedSeasons(seasonList, holder)
    }

    private fun populateCompletedSeasons(seasonList: MutableList<Season>, holder: MainItemViewHolder) : String {
        var completedSeasons = 0

        try {
            seasonList.forEach{
                if(it.isCompleted(it.seenEpisodes, it.totalEpisodes)) completedSeasons++
            }
        } catch (e: Exception) {
            Log.e(Constants.logger, "La lista delle stagioni è vuota o nulla. ${e.message}")
            return "0/0"
        }

        if(completedSeasons == seasonList.size)
            holder.completedImage.backgroundTintList = ContextCompat.getColorStateList(context, R.color.light_blue)
        else
            holder.completedImage.backgroundTintList = ContextCompat.getColorStateList(context, R.color.grey)

        dataSet[holder.adapterPosition].seasons = seasonList

        return "$completedSeasons/${seasonList.size}"
    }

    fun deleteLastSeason(position: Int) {
        dataSet[position].seasons.removeAt(dataSet[position].seasons.size - 1)
        this.notifyItemChanged(position)
        ReadWriteJson.getInstance().write(context, false)
    }

    private fun setRecyclerAdapter(recyclerView: RecyclerView, holder: MainItemViewHolder) {
        val adapter = SeasonRecyclerAdapter(dataSet[holder.adapterPosition],
            this, holder.adapterPosition, holder, context, showTypeEnum)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    private fun isStatusEnabled() : Boolean {
        return ShowTypeEnum.MANGA == showTypeEnum
    }

    override fun getItemCount() = dataSet.size
}