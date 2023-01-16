package com.angiuprojects.gamecatalog.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.utilities.Constants

class MangaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manga)

        val list = Constants.getInstance().getInstanceManga()?.sortedBy { it.name }?.toMutableList()
        //list?.let { setRecyclerAdapter(it) }
    }
}