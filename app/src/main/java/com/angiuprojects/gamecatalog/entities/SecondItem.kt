package com.angiuprojects.gamecatalog.entities

interface SecondItem <T> {

    var volumes : MutableList<T>
    var completed : Boolean
    var name : String

    fun isCompleted(items: MutableList<T>) : Boolean
}