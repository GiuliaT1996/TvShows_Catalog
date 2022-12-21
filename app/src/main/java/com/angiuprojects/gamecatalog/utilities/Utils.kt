package com.angiuprojects.gamecatalog.utilities

import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView

class Utils {

    companion object {

        private lateinit var singleton: Utils

        fun initializeUtilsSingleton(): Utils {
            singleton = Utils()
            return singleton
        }

        fun getInstance(): Utils {
            return singleton
        }
    }

    fun onClickExpandCollapse(recyclerView: RecyclerView, expandButton: ImageButton) {
        if(recyclerView.visibility == View.GONE) {
            //expand
            recyclerView.visibility = View.VISIBLE
            expandButton.rotation = 180F
        } else {
            recyclerView.visibility = View.GONE
            expandButton.rotation = -90F
        }
    }
}