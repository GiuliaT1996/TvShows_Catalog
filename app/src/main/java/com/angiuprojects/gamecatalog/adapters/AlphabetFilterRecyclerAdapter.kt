package com.angiuprojects.gamecatalog.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.activities.ShowsOpenActivity
import com.angiuprojects.gamecatalog.entities.MainItem
import com.angiuprojects.gamecatalog.entities.implementation.Anime
import com.angiuprojects.gamecatalog.enums.ShowTypeEnum
import kotlin.reflect.full.isSubclassOf

class AlphabetFilterRecyclerAdapter <T> (private val dataSet : CharArray,
                                         private val listToFilter : MutableList<T>,
                                         private val activity: ShowsOpenActivity,
                                         private val showTypeEnum: ShowTypeEnum)
    : RecyclerView.Adapter<AlphabetFilterRecyclerAdapter.AlphabeticViewHolder>() {

    class AlphabeticViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var letterButton: ImageView
        var letterText: TextView

        init {
            letterButton = view.findViewById(R.id.letter_button)
            letterText = view.findViewById(R.id.letter_text)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlphabeticViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.letter_view, parent, false)

        return AlphabeticViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlphabeticViewHolder, position: Int) {
        holder.letterText.text = dataSet[holder.adapterPosition].toString().trim()
        holder.letterButton.setOnClickListener {
            val filteredList: MutableList<T>
            if(listToFilter.isNotEmpty()) {
                filteredList = if(listToFilter[0]!!::class.isSubclassOf(MainItem::class)) {
                    listToFilter as MutableList<MainItem>
                    listToFilter.filter { it.name.startsWith(dataSet[holder.adapterPosition].toString().trim(), false) }
                        .sortedBy { it.name }.toMutableList()
                } else {
                    listToFilter as MutableList<Anime>
                    listToFilter.filter { it.name.startsWith(dataSet[holder.adapterPosition].toString().trim(), false) }
                        .sortedBy { it.name }.toMutableList()
                }
               activity.setFilteredRecyclerView(filteredList, showTypeEnum)
            }
        }
    }

    override fun getItemCount() = dataSet.size
}