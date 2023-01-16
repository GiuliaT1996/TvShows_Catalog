package com.angiuprojects.gamecatalog.entities

import com.angiuprojects.gamecatalog.entities.implementation.Season

interface SecondItem {

    var seasons : MutableList<Season>
    var completed : Boolean
    var name : String

    fun isCompleted(items: MutableList<Season>) : Boolean
}