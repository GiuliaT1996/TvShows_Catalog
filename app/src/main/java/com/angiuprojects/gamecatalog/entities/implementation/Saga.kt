package com.angiuprojects.gamecatalog.entities.implementation

import com.angiuprojects.gamecatalog.entities.MainItem

class Saga  : MainItem {
    override var name : String = ""
    override var seasons : MutableList<Season> = mutableListOf()
    override var completed: Boolean = false

    constructor()

    constructor(name: String, seasons: MutableList<Season>) {
        this.name = name
        this.seasons = seasons
        completed = isCompleted(seasons)
    }

    override fun isCompleted (items: MutableList<Season>) : Boolean {
        return !items.any { x -> !x.isCompleted(x) }
    }
}