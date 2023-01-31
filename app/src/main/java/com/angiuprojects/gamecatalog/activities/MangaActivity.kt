package com.angiuprojects.gamecatalog.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.utilities.Constants
import com.angiuprojects.gamecatalog.utilities.ReadWriteJson
import com.angiuprojects.gamecatalog.utilities.ShowTypeEnum
import com.angiuprojects.gamecatalog.utilities.Utils

class MangaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manga)

        val recyclerView = findViewById<RecyclerView>(R.id.tv_show_recycler_view)

        val list = Constants.user?.mangaList?.sortedBy { it.name }?.toMutableList()
        list?.let { Utils.getInstance().setMainItemRecyclerAdapter(it, this, recyclerView, ShowTypeEnum.MANGA) }

        findViewById<ImageButton>(R.id.add_button).setOnClickListener{
            Utils.getInstance().onClickChangeActivity(
            AddActivity::class.java, this, true, ShowTypeEnum.MANGA.toString())}
    }

    override fun onStop() {
        ReadWriteJson.getInstance().write(this, false)
        super.onStop()
    }
}