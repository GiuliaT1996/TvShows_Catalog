package com.angiuprojects.gamecatalog.activities

import android.content.Context
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.adapters.AlphabetFilterRecyclerAdapter
import com.angiuprojects.gamecatalog.adapters.AnimeRecyclerAdapter
import com.angiuprojects.gamecatalog.adapters.FatherRecyclerAdapter
import com.angiuprojects.gamecatalog.entities.MainItem
import com.angiuprojects.gamecatalog.entities.User
import com.angiuprojects.gamecatalog.entities.implementation.Anime
import com.angiuprojects.gamecatalog.enums.ShowTypeEnum
import com.angiuprojects.gamecatalog.utilities.Constants
import com.angiuprojects.gamecatalog.utilities.ReadWriteJson
import com.angiuprojects.gamecatalog.utilities.Utils
import kotlin.reflect.full.isSubclassOf

open class ShowsOpenActivity : AppCompatActivity() {

    private var alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()

    fun setAlphabetRecyclerAdapter(listToFilter : MutableList<*>,
                                   activity: ShowsOpenActivity,
                                   context: Context,
                                   showTypeEnum: ShowTypeEnum) : Boolean {
        val recyclerView = findViewById<RecyclerView>(R.id.horizontal_recycler_view)
        if(recyclerView.visibility == View.VISIBLE) {
            recyclerView.visibility = View.GONE
            return true
        }
        recyclerView.visibility = View.VISIBLE
        val adapter = AlphabetFilterRecyclerAdapter(alphabet, listToFilter, activity, showTypeEnum)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        return false
    }

    fun <T> setShowsRecyclerView(listGetter: java.util.function.Function<User, MutableList<T>>,
                                 nameGetter: java.util.function.Function<T, String>,
                                 showTypeEnum: ShowTypeEnum) : MutableList<T> {

        val recyclerView = findViewById<RecyclerView>(R.id.shows_recycler_view)
        var list : MutableList<T> = listGetter.apply(Constants.user!!)
        if(Constants.user != null && list.isNotEmpty()) {
            list = list.sortedBy { nameGetter.apply(it) }.toMutableList()
            if(list[0]!!::class.isSubclassOf(MainItem::class)) Utils.getInstance().setMainItemRecyclerAdapter(
                list as MutableList<out MainItem>, this,
                recyclerView, showTypeEnum)
            else setAnimeRecyclerAdapter(list as MutableList<Anime>,
                this, recyclerView)
        }
        return list
    }

    fun <T> setFilteredRecyclerView(list: MutableList<T>, showTypeEnum: ShowTypeEnum) {
        val recyclerView = findViewById<RecyclerView>(R.id.shows_recycler_view)
        if(showTypeEnum != ShowTypeEnum.ANIME) {
            Utils.getInstance().setMainItemRecyclerAdapter(
                list as MutableList<out MainItem>, this,
                recyclerView, showTypeEnum)
        } else setAnimeRecyclerAdapter(list as MutableList<Anime>,
            this, recyclerView)
    }

    fun onClickStartAddActivity(showTypeEnum: ShowTypeEnum) {
        findViewById<ImageButton>(R.id.add_button).setOnClickListener {
            Utils.getInstance().onClickChangeActivity(
                AddActivity::class.java, this, true, showTypeEnum.type)}
    }

    private fun setAnimeRecyclerAdapter(itemList: MutableList<Anime>,
                                        context: Context,
                                        recyclerView: RecyclerView
    ) {

        val adapter = AnimeRecyclerAdapter(itemList, context)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun onStop() {
        ReadWriteJson.getInstance().write(this, false)
        super.onStop()
    }
}