package com.angiuprojects.gamecatalog.entities.implementation

import com.angiuprojects.gamecatalog.entities.FirstItem

class Season : FirstItem {

    override var completed: Boolean = false
    override var number: Int = 0
    override var seenEpisodes: Int = 0
    override var totalEpisodes: Int = 0
    override var name: String = ""

    constructor()

    constructor(number: Int, seenEpisodes: Int, totalEpisodes: Int) {
        this.number = number
        this.name = "Stagione $number"
        this.seenEpisodes = seenEpisodes
        this.totalEpisodes = totalEpisodes
        this.completed = isCompleted(seenEpisodes, totalEpisodes)
    }

    override fun isCompleted(seen: Int, total: Int): Boolean {
        return seen == total
    }
}