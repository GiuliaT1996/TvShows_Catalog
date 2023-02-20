package com.angiuprojects.gamecatalog.entities.implementation

class Anime {

    var name: String = ""
    var hasSaga : Boolean = false
    var sagas: MutableList<Saga> = mutableListOf()
    var completed : Boolean = false

    constructor()

    constructor(name: String, sagas: MutableList<Saga>, hasSaga: Boolean) {
        this.name = name
        this.hasSaga = hasSaga
        this.sagas = sagas
        completed = isCompleted(sagas)
    }

    private fun isCompleted(items: List<Saga>): Boolean {
        return !items.any { x -> !x.completed }
    }

}