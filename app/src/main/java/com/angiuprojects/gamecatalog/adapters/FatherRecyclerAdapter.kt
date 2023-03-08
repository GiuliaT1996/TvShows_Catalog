package com.angiuprojects.gamecatalog.adapters

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.entities.implementation.Anime
import com.angiuprojects.gamecatalog.entities.implementation.Manga
import com.angiuprojects.gamecatalog.entities.implementation.Season
import com.angiuprojects.gamecatalog.enums.MangaStatusEnum
import com.angiuprojects.gamecatalog.enums.ShowTypeEnum
import com.angiuprojects.gamecatalog.utilities.ReadWriteJson
import com.angiuprojects.gamecatalog.utilities.Utils
import java.util.function.BiConsumer
import java.util.function.Function
import kotlin.reflect.KMutableProperty1

open class FatherRecyclerAdapter<T> (private var dataSet: MutableList<out T>,
                                     val fatherContext: Context)
    : RecyclerView.Adapter<FatherRecyclerAdapter.MainItemViewHolder>() {

    lateinit var dialog : Dialog

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_item_view, parent, false)

        dialog = Dialog(fatherContext)

        return MainItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainItemViewHolder, position: Int) {}

    fun onLongClickOpenEditPopUp(
        position: Int,
        item: T,
        name: String,
        holder: MainItemViewHolder,
        isStatusEnabled: Boolean,
        showTypeEnum: ShowTypeEnum,
        itemGetter: Function<T, String>,
        itemSetter: BiConsumer<T, String>
    ) {

        val inflater = fatherContext.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var popUpView: View = inflater.inflate(R.layout.update_delete_show_popup, null)

        var status: AutoCompleteTextView? = null

        if(isStatusEnabled && dataSet[holder.adapterPosition] is Manga) {
            popUpView = inflater.inflate(R.layout.update_delete_manga_popup, null)
            status = handlePopUpStatus(popUpView, dataSet[holder.adapterPosition] as Manga)
        }

        dialog.setContentView(popUpView)

        val newName = popUpView.findViewById<AutoCompleteTextView>(R.id.name_auto_complete)
        newName.setText(name)

        val updateButton = popUpView.findViewById<Button>(R.id.update)
        updateButton.setOnClickListener{ updateShowsName(position, newName.text.toString().trim(),
            item, status, popUpView, showTypeEnum, itemGetter, itemSetter) }

        val removeButton = popUpView.findViewById<Button>(R.id.remove)
        removeButton.setOnClickListener{deleteItem(position)}

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun updateShowsName(position: Int, newName: String, item: T,
                             status: AutoCompleteTextView?, popUpView: View,
                             showTypeEnum: ShowTypeEnum, itemGetter: Function<T, String>,
                             itemSetter: BiConsumer<T, String>) {
        if(status != null && item is Manga)
            item.status = MangaStatusEnum.getMangaStatusEnum(status.text.toString().trim())

        if(newName.isNotEmpty() && newName != itemGetter.apply(item).trim()) {
            if(!Utils.getInstance().launchSnackBar(!Utils.getInstance()
                    .checkIfNameAlreadyExists(newName, position, showTypeEnum),
                    "Nome già esistente!", popUpView))
                itemSetter.accept(item, newName)
        }
        this.notifyItemChanged(position)
        ReadWriteJson.getInstance().write(fatherContext, false)
        dialog.dismiss()
    }

    fun updateDataset(newDataSet: MutableList<out T>){
        dataSet = newDataSet
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        this.notifyItemRemoved(position)
        dataSet.removeAt(position)
        ReadWriteJson.getInstance().write(fatherContext, false)
        dialog.dismiss()
    }

    private fun handlePopUpStatus(popUpView: View, manga: Manga?) : AutoCompleteTextView {
        val filterSpinner = popUpView.findViewById<AutoCompleteTextView>(R.id.status_spinner)
        filterSpinner.setText(manga?.status?.status ?: "")
        Utils.getInstance().assignAdapterToSpinner(filterSpinner, fatherContext)
        return filterSpinner
    }

    open fun updateCompletedSeasons(holder: MainItemViewHolder,
                                    item: T) {}

    override fun getItemCount() = dataSet.size
}