package com.angiuprojects.gamecatalog.entities.implementation

import com.angiuprojects.gamecatalog.entities.SecondItem

class Season : SecondItem<Episode> {

    override var completed: Boolean = false
    override var number: Int = 0
    override var episodes: MutableList<Episode> = mutableListOf()
    override var name: String = ""

    constructor(number: Int, name: String, episodes: MutableList<Episode>) {
        this.number = number
        this.name = name
        this.episodes = episodes
        this.completed = isCompleted(episodes)
    }

    override fun isCompleted(items: MutableList<Episode>): Boolean {
        return !items.any { x -> !x.seenOrRead }
    }
}