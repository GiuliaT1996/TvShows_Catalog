package com.angiuprojects.gamecatalog.entities.implementation

import com.angiuprojects.gamecatalog.entities.MainItem

class TVShow(override var name: String, override var seasons: MutableList<Season>) : MainItem {

    override var completed: Boolean = false

    init {
        completed = isCompleted(seasons)
    }

    override fun isCompleted (items: MutableList<Season>) : Boolean {
        return !items.any { x -> !x.completed }
    }
}