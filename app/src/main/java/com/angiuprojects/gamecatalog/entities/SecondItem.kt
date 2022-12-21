package com.angiuprojects.gamecatalog.entities

interface SecondItem<T> {

    var number : Int
    var name : String
    var episodes : MutableList<T>
    var completed : Boolean

    fun isCompleted(items: MutableList<T>) : Boolean
}