package com.angiuprojects.gamecatalog.entities.implementation

import com.angiuprojects.gamecatalog.entities.ThirdItem

class TVShow : ThirdItem<Season> {

    override var name : String = ""
    override var volumes : MutableList<Season> = mutableListOf()
    override var completed: Boolean = false

    constructor(name: String, seasons: MutableList<Season>) {
        this.name = name
        this.volumes = seasons
        completed = isCompleted(seasons)
    }

    override fun isCompleted (items: MutableList<Season>) : Boolean {
        return !items.any { x -> !x.completed }
    }
}