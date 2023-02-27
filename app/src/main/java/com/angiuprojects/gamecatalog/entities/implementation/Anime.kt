package com.angiuprojects.gamecatalog.entities.implementation

class Anime(var name: String, var sagas: MutableList<Saga>, var hasSaga: Boolean) {

    private var completed : Boolean = false

    init {
        completed = isCompleted(sagas)
    }

    private fun isCompleted(items: List<Saga>): Boolean {
        return !items.any { x -> !x.completed }
    }

}