package com.angiuprojects.gamecatalog.entities.implementation

class Season {

    var completed: Boolean = false
    var number: Int = 0
    var seenEpisodes: Int = 0
    var totalEpisodes: Int = 0
    var name: String = ""

    constructor()

    constructor(number: Int, seenEpisodes: Int, totalEpisodes: Int) {
        this.number = number
        this.name = "Stagione $number"
        this.seenEpisodes = seenEpisodes
        this.totalEpisodes = totalEpisodes
        this.completed = isCompleted(seenEpisodes, totalEpisodes)
    }

    fun isCompleted(seen: Int, total: Int): Boolean {
        return seen == total
    }
}