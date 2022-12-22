package com.angiuprojects.gamecatalog.entities.implementation

import com.angiuprojects.gamecatalog.entities.SecondItem

class TVShow : SecondItem<Season> {

    override var name : String = ""
    override var volumes : MutableList<Season> = mutableListOf()
    override var completed: Boolean = false

    constructor()

    constructor(name: String, seasons: MutableList<Season>) {
        this.name = name
        this.volumes = seasons
        completed = isCompleted(seasons)
    }

    override fun isCompleted (items: MutableList<Season>) : Boolean {
        return !items.any { x -> !x.completed }
    }
}