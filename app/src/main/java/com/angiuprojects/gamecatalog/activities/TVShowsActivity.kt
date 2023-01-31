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

class TVShowsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tvshows)

        val recyclerView = findViewById<RecyclerView>(R.id.tv_show_recycler_view)

        val list = Constants.user?.tvShowList?.sortedBy { it.name }?.toMutableList()
        list?.let { Utils.getInstance().setMainItemRecyclerAdapter(it, this, recyclerView, ShowTypeEnum.TV_SHOW) }

        findViewById<ImageButton>(R.id.add_button).setOnClickListener{
            Utils.getInstance().onClickChangeActivity(
                AddActivity::class.java, this, true, ShowTypeEnum.TV_SHOW.toString())}
    }

    override fun onStop() {
        ReadWriteJson.getInstance().write(this, false)
        super.onStop()
    }

}