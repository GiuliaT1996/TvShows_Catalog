package com.angiuprojects.gamecatalog.entities.implementation

import com.angiuprojects.gamecatalog.entities.SecondItem

class Volume : SecondItem<Chapter> {

    override var completed: Boolean = false
    override var number: Int = 0
    override var episodes: MutableList<Chapter> = mutableListOf()
    override var name: String = ""

    constructor(number: Int, name: String, chapters: MutableList<Chapter>) {
        this.number = number
        this.name = name
        this.episodes = chapters
        this.completed = isCompleted(chapters)
    }

    override fun isCompleted(items: MutableList<Chapter>): Boolean {
        return !items.any { x -> !x.seenOrRead }
    }
}