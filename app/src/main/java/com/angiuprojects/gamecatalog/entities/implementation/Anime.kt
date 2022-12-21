package com.angiuprojects.gamecatalog.entities.implementation

class Anime {

    var hasSaga : Boolean = false
    var sagas: MutableList<Saga> = mutableListOf()
    var completed : Boolean = false

    constructor(sagas: MutableList<Saga>) {
        hasSaga = true
        this.sagas = sagas
        completed = isCompleted(sagas)
    }

    constructor(saga: Saga) {
        hasSaga = false
        sagas.add(saga)
        completed = saga.completed
    }

    private fun isCompleted(items: List<Saga>): Boolean {
        return !items.any { x -> !x.completed }
    }

}